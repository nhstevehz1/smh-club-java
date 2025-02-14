package com.smh.club.api.rest.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.*;
import com.smh.club.api.rest.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.time.LocalDate;
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
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

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
        var create = Instancio.of(CreateMemberDto.class)
            .ignore(field(MemberDto::getId))
            .ignore(field(AddressDto::getMemberId))
            .ignore(field(EmailDto::getMemberId))
            .ignore(field(PhoneDto::getMemberId))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .set(field(MemberDto::getBirthDate), LocalDate.now().minusYears(22))
            .create();

        // perform POST
        var ret = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(MemberDto::getFirstName)),
            arguments(field(MemberDto::getLastName)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .ignore(field(MemberDto::getId))
            .setBlank(nonNullableField)
            .generate(field(MemberDto::getBirthDate),
                g -> g.temporal().localDate().range(LocalDate.now().minusYears(100),
                                LocalDate.now().minusYears(21)))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .ignore(field(MemberDto::getId))
            .setBlank(field(MemberDto::getBirthDate))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_joinedDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .ignore(field(MemberDto::getId))
            .setBlank(field(MemberDto::getJoinedDate))
            .generate(field(MemberDto::getBirthDate),
                g -> g.temporal().localDate().range(LocalDate.now().minusYears(100),
                    LocalDate.now().minusYears(21)))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    private static Stream<Arguments> nullableFields() {
        return Stream.of(
            arguments(field(MemberDto::getMiddleName)),
            arguments(field(MemberDto::getSuffix)));
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
        // create address
        var create = Instancio.of(CreateMemberDto.class)
            .setBlank(nullableField)
            .ignore(field(MemberDto::getId))
            .ignore(field(AddressDto::getMemberId))
            .ignore(field(EmailDto::getMemberId))
            .ignore(field(PhoneDto::getMemberId))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform POST
        var ret = sendValidCreate(create, MemberDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void create_with_invalid_memberNumber_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .set(field(MemberDto::getMemberNumber), 0)
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .ignore(field(MemberDto::getId))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .set(field(MemberDto::getBirthDate), LocalDate.now())
            .ignore(field(MemberDto::getId))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_joinedDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(CreateMemberDto.class)
            .set(field(MemberDto::getJoinedDate), LocalDate.now().minusYears(10))
            .set(field(MemberDto::getBirthDate), LocalDate.now().minusYears(22))
            .ignore(field(MemberDto::getId))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_dto_with_status_ok() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, MemberDto.class);

        // verify
        var member = repo.findById(id);

        assertTrue(member.isPresent());
        assertEquals(update, actual);
        verify(update, member.get());
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(MemberDto.class);

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
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .setBlank(nonNullableField)
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .setBlank(field(MemberDto::getBirthDate))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_joinedDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .setBlank(field(MemberDto::getJoinedDate))
            .generate(field(MemberDto::getBirthDate),
                g -> g.temporal().localDate().range(LocalDate.now().minusYears(100),
                    LocalDate.now().minusYears(21)))
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
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .setBlank(nullableField)
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, MemberDto.class);

        // verify
        var member = repo.findById(id);

        assertTrue(member.isPresent());
        assertEquals(update, actual);
        verify(update, member.get());
    }

    @Test
    public void update_with_invalid_birthDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .set(field(MemberDto::getBirthDate), LocalDate.now().minusYears(10))
            .set(field(MemberDto::getJoinedDate), LocalDate.now())
            .create();

        // perform put
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_joinedDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberDto.class)
            .set(field(MemberDto::getId), id)
            .set(field(MemberDto::getBirthDate), entity.getBirthDate())
            .set(field(MemberDto::getJoinedDate), entity.getBirthDate().minusYears(1))
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
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(CreateMemberDto expected, MemberEntity actual) {
        verify(expected.getMember(), actual);

        assertEquals(1, actual.getAddresses().size());
        var actAddress = actual.getAddresses().getFirst();
        var expAddress = expected.getAddress();
        assertEquals(expAddress.getAddress1(), actAddress.getAddress1());
        assertEquals(expAddress.getAddress2(), actAddress.getAddress2());
        assertEquals(expAddress.getCity(), actAddress.getCity());
        assertEquals(expAddress.getState(), actAddress.getState());
        assertEquals(expAddress.getZip(), actAddress.getZip());
        assertEquals(expAddress.getAddressType(), actAddress.getAddressType());

        assertEquals(1, actual.getEmails().size());
        var actEmail = actual.getEmails().getFirst();
        var expEmail = expected.getEmail();
        assertEquals(expEmail.getEmail(), actEmail.getEmail());
        assertEquals(expEmail.getEmailType(), actEmail.getEmailType());

        assertEquals(1, actual.getPhones().size());
        var actPhone = actual.getPhones().getFirst();
        var expPhone = expected.getPhone();
        assertEquals(expPhone.getPhoneNumber(), actPhone.getPhoneNumber());
        assertEquals(expPhone.getPhoneType(), actPhone.getPhoneType());

        assertTrue(actual.getRenewals().isEmpty());
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
