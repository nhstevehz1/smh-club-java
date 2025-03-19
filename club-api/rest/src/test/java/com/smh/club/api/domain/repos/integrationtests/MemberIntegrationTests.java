package com.smh.club.api.domain.repos.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.address.AddressCreateDto;
import com.smh.club.api.dto.email.EmailCreateDto;
import com.smh.club.api.dto.member.MemberBaseDto;
import com.smh.club.api.dto.member.MemberCreateDto;
import com.smh.club.api.dto.member.MemberDto;
import com.smh.club.api.dto.member.MemberUpdateDto;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.instancio.Instancio;
import org.instancio.Selector;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Mode;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@WithMockUser(authorities = {"ROLE_club-admin", "ROLE_club-user"})
@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests extends IntegrationTests {

    // need to mock the decoder otherwise an initialization error is thrown
    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Value("${spring.data.web.pageable.default-page-size:20}")
    private int defaultPageSize;

    @Value("${spring.data.rest.sort-param-name:sort}")
    private String sortParamName;

    @Value("${spring.data.rest.size-param-name:size}")
    private String sizeParamName;

    @Value("${spring.data.rest.page-param-name:page}")
    private String pageParamName;

    @Autowired
    private MembersRepo repo;

    @WithSettings // Instancio settings
    private final Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true)
            .set(Keys.MODE, Mode.LENIENT);

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc,mapper, "/api/v1/members");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();

        var testParams = PageTestParams.of(MemberDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName,  "member_number,desc");

        var testParams = PageTestParams.of(MemberDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) {
        addEntitiesToDb(15);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(MemberDto.class, map, path, sorted.size(),
            0, pageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    public void getListPage_page(int page) {
        var entitySize = 150;
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(MemberDto.class, map, path, sorted.size(),
            page, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"id,id", "member_number,memberNumber", "first_name,firstName",
        "last_name,lastName", "birth_date,birthDate", "joined_date,joinedDate" })
    public void getListPage_sortColumn(String sort, String entitySort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var pageRequest = PageRequest.of(0, entitySize, Sort.by(entitySort));
        var expected = repo.findAll(pageRequest);
        assertEquals(expected.getContent(), expected.getContent().stream().sorted(sortFields.getEntity()).toList());

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);
        map.put(sizeParamName, String.valueOf(entitySize));

        var testParams = PageTestParams.of(MemberDto.class, map, path, expected.getTotalElements(),
            0, entitySize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

        for(int ii = 0; ii < entitySize; ii++) {
            verify(expected.getContent().get(ii), actual.get(ii));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"middle_name", "suffix"})
    public void getListPage_excluded_fields_returns_bad_request(String sort) {
        // setup
        Map<String, String > map = new HashMap<>();
        map.put(sortParamName, sort);

        // execute and verify
        given()
            .auth().none()
            .params(map)
            .when()
            .get(path)
            .then().assertThat()
            .status(HttpStatus.BAD_REQUEST)
            .expect(jsonPath("$.validation-errors").isNotEmpty());

    }

    @Test
    public void get_returns_dto_status_ok() {
        // setup
        var email = addEntitiesToDb(20).get(10);
        var id = email.getId();

        // perform get
        var actual =
            given()
                .auth().none()
                .pathParam("id", id)
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get(path + "/{id}")
                .then()
                .assertThat().status(HttpStatus.OK)
                .assertThat().contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(MemberDto.class);

        verify(email, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        // Setup
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(MemberEntity::getId)).map(MemberEntity::getId).orElseThrow();
        var id = highest + 100;

        // perform get and verify
        given()
            .auth().none()
            .pathParam("id", id)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get( path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create_returns_dto_status_created() throws Exception {
        var create = Instancio.of(MemberCreateDto.class)
            .withSetting(Keys.COLLECTION_MAX_SIZE, 1)
            .withSetting(Keys.COLLECTION_MIN_SIZE, 1)
            .ignore(field(AddressCreateDto::getMemberId))
            .ignore(field(EmailCreateDto::getMemberId))
            .ignore(field(PhoneCreateDto::getMemberId))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .set(field(MemberCreateDto::getBirthDate), ZonedDateTime.now().minusYears(22).toInstant())
            .create();

        // perform POST
        var actual = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(actual.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
        verify(actual, entity.get());
    }

    @Test
    public void create_returns_memberNumber_equal_to_one() throws Exception {
        // when table is empty, the member service will assign 1 to the member number
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .ignore(field(AddressCreateDto::getMemberId))
            .ignore(field(EmailCreateDto::getMemberId))
            .ignore(field(PhoneCreateDto::getMemberId))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .set(field(MemberCreateDto::getBirthDate), ZonedDateTime.now().minusYears(22).toInstant())
            .create();

        // perform POST
        var ret = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getMemberNumber());
    }

    @Test
    public void create_returns_with_memberNumber_equal_to_next_available() throws Exception {
        // setup
        // when the available numbers has a gap ex: {1,2,3,5,6,...},
        // the member service will assign 4 to the member number
        var create = Instancio.of(MemberCreateDto.class)
            .ignore(field(AddressCreateDto::getMemberId))
            .ignore(field(EmailCreateDto::getMemberId))
            .ignore(field(PhoneCreateDto::getMemberId))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .set(field(MemberCreateDto::getBirthDate), ZonedDateTime.now().minusYears(22).toInstant())
            .create();

        // perform POST
        var ret = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getMemberNumber());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(MemberCreateDto::getFirstName)),
            arguments(field(MemberCreateDto::getLastName)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .setBlank(nonNullableField)
            .generate(field(MemberCreateDto::getBirthDate),
                g -> g.temporal().instant().range(
                    ZonedDateTime.now().minusYears(100).toInstant(),
                    ZonedDateTime.now().minusYears(21).toInstant()))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .setBlank(field(MemberCreateDto::getBirthDate))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_joinedDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .setBlank(field(MemberCreateDto::getJoinedDate))
            .generate(field(MemberCreateDto::getBirthDate),
                g -> g.temporal().instant().range(
                    ZonedDateTime.now().minusYears(100).toInstant(),
                    ZonedDateTime.now().minusYears(21).toInstant()))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }



    private static Stream<Arguments> nullableFields() {
        return Stream.of(
            arguments(field(MemberCreateDto::getMiddleName)),
            arguments(field(MemberCreateDto::getSuffix)));
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
        // create address
        var create = Instancio.of(MemberCreateDto.class)
            .withSetting(Keys.COLLECTION_MAX_SIZE, 1)
            .withSetting(Keys.COLLECTION_MIN_SIZE, 1)
            .setBlank(nullableField)
            .ignore(field(AddressCreateDto::getMemberId))
            .ignore(field(EmailCreateDto::getMemberId))
            .ignore(field(PhoneCreateDto::getMemberId))
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        var actual = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(actual.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
        verify(actual, entity.get());
    }

    @Test
    public void create_with_invalid_memberNumber_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .set(field(MemberCreateDto::getMemberNumber), -1)
            .set(field(MemberCreateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberCreateDto.class)
            .set(field(MemberCreateDto::getBirthDate), Instant.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_joinedDate_returns_bad_request() throws Exception {
        // setup
        var joinedDate = ZonedDateTime.now().minusYears(10).toInstant();
        var birthDate = ZonedDateTime.now().minusYears(22).toInstant();
        var create = Instancio.of(MemberCreateDto.class)
            .set(field(MemberCreateDto::getJoinedDate), joinedDate)
            .set(field(MemberCreateDto::getBirthDate), birthDate)
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_dto_with_status_ok() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getJoinedDate), Instant.now())
            .set(field(MemberUpdateDto::getId), id)
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, MemberDto.class);

        // verify
        var member = repo.findById(id);

        assertTrue(member.isPresent());
        verify(update, member.get());
        verify(actual, member.get());
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(MemberUpdateDto.class);

        // perform put
        given()
            .auth().none()
            .accept(MediaType.APPLICATION_JSON)
            .pathParam("id", update.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
            .when()
            .put(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST);

    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void update_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .setBlank(nonNullableField)
            .set(field(MemberUpdateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .setBlank(field(MemberUpdateDto::getBirthDate))
            .set(field(MemberUpdateDto::getJoinedDate), Instant.now())
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_joinedDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .setBlank(field(MemberUpdateDto::getJoinedDate))
            .generate(field(MemberUpdateDto::getBirthDate),
                g -> g.temporal().instant().range(
                    ZonedDateTime.now().minusYears(100).toInstant(),
                    ZonedDateTime.now().minusYears(21).toInstant()))
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void update_with_nullable_files_returns_dto_status_ok(Selector nullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .setBlank(nullableField)
            .set(field(MemberUpdateDto::getJoinedDate), Instant.now())
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, MemberDto.class);

        // verify
        var member = repo.findById(id);

        assertTrue(member.isPresent());
        verify(update, member.get());
        verify(actual, member.get());
    }

    @Test
    public void update_with_invalid_birthDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .set(field(MemberUpdateDto::getBirthDate), ZonedDateTime.now().minusYears(10).toInstant())
            .set(field(MemberUpdateDto::getJoinedDate), Instant.now())
            .create();

        // perform put
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_joinedDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberUpdateDto.class)
            .set(field(MemberUpdateDto::getId), id)
            .set(field(MemberUpdateDto::getBirthDate), entity.getBirthDate())
            .set(field(MemberUpdateDto::getJoinedDate),
                entity.getBirthDate().atZone(ZoneId.systemDefault()).minusYears(1).toInstant())
            .create();

        // perform put
        sendInvalidUpdate(id, update);
    }

    @Test
    public void delete_status_noContent() {
        // create several members
        var entities = addEntitiesToDb(10);
        var id = entities.get(5).getId();

        // perform delete
        given()
            .auth().none().pathParam("id", id)
            .when()
            .delete(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NO_CONTENT);

        var email = repo.findById(id);
        assertTrue(email.isEmpty());
    }

    @Test
    public void getCount_returns_count_status_ok() {
        // setup
        var count = addEntitiesToDb(25).size();

        // execute
        var result =
            given()
                .auth().none()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get(path + "/count")
                .then()
                .assertThat(status().isOk())
                .extract().body().as(CountResponse.class);

        assertEquals(count, result.getCount());
    }

    private List<MemberEntity> addEntitiesToDb(int size) {
        var entities = Instancio.ofList(MemberEntity.class)
                .size(size)
                .withUnique(field(MemberEntity::getMemberNumber))
                .ignore(field(MemberEntity::getId))
                .create();

        return repo.saveAllAndFlush(entities);
    }

    private void verify(MemberDto expected, MemberEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        verify((MemberBaseDto) expected, actual);
    }

    private void verify(MemberUpdateDto expected, MemberEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        verify((MemberBaseDto) expected, actual);
    }

    private void verify(MemberCreateDto expected, MemberEntity actual) {
        verify((MemberBaseDto) expected, actual);

        assertEquals(expected.getAddresses().size(), actual.getAddresses().size());
        var actAddress = actual.getAddresses().getFirst();
        var expAddress = expected.getAddresses().getFirst();
        assertEquals(expAddress.getAddress1(), actAddress.getAddress1());
        assertEquals(expAddress.getAddress2(), actAddress.getAddress2());
        assertEquals(expAddress.getCity(), actAddress.getCity());
        assertEquals(expAddress.getState(), actAddress.getState());
        assertEquals(expAddress.getPostalCode(), actAddress.getPostalCode());
        assertEquals(expAddress.getAddressType().getAddressTypeName(),
            actAddress.getAddressType().getAddressTypeName());

        assertEquals(expected.getEmails().size(), actual.getEmails().size());
        var actEmail = actual.getEmails().getFirst();
        var expEmail = expected.getEmails().getFirst();
        assertEquals(expEmail.getEmail(), actEmail.getEmail());
        assertEquals(expEmail.getEmailType().getEmailTypeName(),
            actEmail.getEmailType().getEmailTypeName());

        assertEquals(expected.getPhones().size(), actual.getPhones().size());
        var actPhone = actual.getPhones().getFirst();
        var expPhone = expected.getPhones().getFirst();
        assertEquals(expPhone.getPhoneNumber(), actPhone.getPhoneNumber());
        assertEquals(expPhone.getPhoneType().getPhoneTypeName(),
            actPhone.getPhoneType().getPhoneTypeName());

        assertTrue(actual.getRenewals().isEmpty());
    }

    private void verify(MemberBaseDto expected, MemberEntity actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(MemberEntity expected, MemberDto actual) {
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(List<MemberEntity> expected, List<MemberDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<MemberEntity, MemberDto>> getSorts() {

        Map<String, SortFields<MemberEntity, MemberDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(MemberEntity::getId),
            Comparator.comparingInt(MemberDto::getId)));

        map.put("member_number", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("first_name", SortFields.of(Comparator.comparing(MemberEntity::getFirstName),
            Comparator.comparing(MemberDto::getFirstName)));

        map.put("middle_name", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("last_name", SortFields.of(Comparator.comparing(MemberEntity::getLastName),
            Comparator.comparing(MemberDto::getLastName)));

        map.put("suffix", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("birth_date", SortFields.of(Comparator.comparing(MemberEntity::getBirthDate),
            Comparator.comparing(MemberDto::getBirthDate)));

        map.put("joined_date", SortFields.of(Comparator.comparing(MemberEntity::getJoinedDate),
            Comparator.comparing(MemberDto::getJoinedDate)));

        return map;
    }
}
