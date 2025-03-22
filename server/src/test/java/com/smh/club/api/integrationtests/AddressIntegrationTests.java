package com.smh.club.api.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.dto.address.*;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.Selector;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static java.util.Comparator.comparingInt;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(authorities = {"ROLE_club-admin", "ROLE_club-user"})
@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AddressIntegrationTests extends IntegrationTests {

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
    private MembersRepo memberRepo;

    @Autowired
    private AddressRepo repo;

    @WithSettings // Instancio settings
    Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.BEAN_VALIDATION_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0)
                .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public AddressIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v1/addresses");
    }

    @BeforeEach
    public void init() {
        // there seems to be a bug where @WithSettings is not recognized in @BeforeAll
        var members = Instancio.ofList(MemberEntity.class)
                .size(100)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
        
        memberRepo.saveAllAndFlush(members);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) {
        // populate address table
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        var testParams = PageTestParams.of(AddressFullNameDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(AddressFullNameDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        // populate address table
       addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName,  "id,desc");

        var testParams = PageTestParams.of(AddressFullNameDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressFullNameDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(AddressFullNameDto.class, map, path, sorted.size(),
            0, pageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressFullNameDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    public void getListPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(AddressEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());


        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(AddressFullNameDto.class, map, path, sorted.size(),
            page, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressFullNameDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"id,id", "address1,address1", "city,city", "state,state", "postal_code,postalCode",
        "address_type,addressType", "member_number,member.memberNumber", "full_name,member.lastName" })
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

        var testParams = PageTestParams.of(AddressFullNameDto.class, map, path, expected.getTotalElements(),
            0, entitySize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

        for(int ii = 0; ii < entitySize; ii++) {
            verify(expected.getContent().get(ii), actual.get(ii));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"address2"})
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
        .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .expect(jsonPath("$.validation-errors").isNotEmpty());

    }

    @Test
    public void get_returns_dto_status_ok() {
        // setup
        var address = addEntitiesToDb(20).get(10);
        var id = address.getId();

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
                .extract().body().as(AddressDto.class);

        verify(address, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(AddressEntity::getId)).map(AddressEntity::getId).orElseThrow();
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

    @ParameterizedTest
    @ValueSource(strings = {"#d#d#d#d#d", "#d#d#d#d#d-#d#d#d#d"})
    public void create_returns_dto_status_created(String validPattern) throws Exception {
        // create addresses
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressCreateDto.class)
                .generate(field(AddressDto::getMemberId),
                    g -> g.oneOf(memberIdList))
                .generate(field(AddressDto::getPostalCode), g -> g.text().pattern(validPattern))
                .create();

        // perform POST
        var actual = sendValidCreate(create, AddressDto.class);

        // verify
        var entity =  repo.findById(actual.getId());

        assertTrue(entity.isPresent());
        verify(actual, entity.get());
        verify(create, entity.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullFields() {
        return Stream.of(
            arguments(field(AddressDto::getAddress1)),
            arguments(field(AddressDto::getCity)),
            arguments(field(AddressDto::getState)),
            arguments(field(AddressDto::getState)));
    }

    @ParameterizedTest
    @MethodSource("nonNullFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressCreateDto.class)
            .generate(field(AddressCreateDto::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(nonNullableField)
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_postalCode_returns_bad_request() throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressCreateDto.class)
            .generate(field(AddressCreateDto::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(field(AddressCreateDto::getPostalCode))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    private static Stream<Arguments> nullableFields() {
        return Stream.of(
            arguments(field(AddressDto::getAddress2)));
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
        // create address
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressCreateDto.class)
            .generate(field(AddressCreateDto::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(nullableField)
            .create();

        // perform POST
        var actual = sendValidCreate(create, AddressDto.class);

        // verify
        var entity =  repo.findById(actual.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
        verify(actual, entity.get());
    }

    @Test
    public void create_with_invalid_zip_returns_bad_request() throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressCreateDto.class)
            .set(field(AddressCreateDto::getPostalCode), "AAA")
            .generate(field(AddressCreateDto::getMemberId),
                g -> g.oneOf(memberIdList))
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d#d#d#d#d", "#d#d#d#d#d-#d#d#d#d"})
    public void update_returns_addressDto_status_ok(String pattern) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var memberId = entity.getMember().getId();
        var id = entity.getId();
        var update =
            Instancio.of(AddressDto.class)
                .set(field(AddressDto::getId), id)
                .set(field(AddressDto::getMemberId), memberId)
                .generate(field(AddressDto::getPostalCode), g -> g.text().pattern(pattern))
                .create();

        // perform PUT
        var actual = sendValidUpdate(id, update, AddressDto.class);

        // verify
        var address = repo.findById(id);

        assertTrue(address.isPresent());
        verify(update, address.get());
        verify(actual, address.get());
    }

    @ParameterizedTest
    @MethodSource("nonNullFields")
    public void update_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var memberId = entity.getMember().getId();
        var id = entity.getId();
        var update = Instancio.of(AddressDto.class)
            .set(field(AddressDto::getId), id)
            .set(field(AddressDto::getMemberId), memberId)
            .setBlank(nonNullableField)
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_postalCode_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var memberId = entity.getMember().getId();
        var id = entity.getId();
        var update = Instancio.of(AddressDto.class)
            .set(field(AddressDto::getId), id)
            .set(field(AddressDto::getMemberId), memberId)
            .setBlank(field(AddressDto::getPostalCode))
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void update_nullableField_returns_dto_status_ok(Selector nullableField) throws Exception {
        // create address
        var entity = addEntitiesToDb(5).get(2);
        var memberId = entity.getMember().getId();
        var id = entity.getId();
        var update = Instancio.of(AddressDto.class)
            .set(field(AddressDto::getId), id)
            .set(field(AddressDto::getMemberId), memberId)
            .ignore(nullableField)
            .create();

        // perform POST
        var actual = sendValidUpdate(id, update, AddressDto.class);

        // verify
        var address =  repo.findById(id);

        assertTrue(address.isPresent());
        verify(update, address.get());
        verify(actual, address.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d","#d#d","#d#d#d","#d#d#d#d", "#d#d#d#d#d-#d#d#d", "#c#c"})
    public void update_with_invalid_postalCode_returns_bad_request(String pattern) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var memberId = entity.getMember().getId();
        var id = entity.getId();
        var update = Instancio.of(AddressDto.class)
            .set(field(AddressDto::getId), id)
            .set(field(AddressDto::getMemberId), memberId)
            .generate(field(AddressDto::getPostalCode), g -> g.text().pattern(pattern))
            .create();

        // perform POST
        sendInvalidUpdate(id, update);
    }

    @Test
    public void deleteAddress_status_noContent() {
        // create several addresses
        var entities = addEntitiesToDb(5);
        var id = entities.get(2).getId();

        // perform DELETE
        given()
            .auth().none().pathParam("id", id)
            .when()
            .delete(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NO_CONTENT);

        // verify
        var address = repo.findById(id);
        assertTrue(address.isEmpty());
    }

    public List<AddressEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(AddressEntity.class)
                .size(size)
                .ignore(field(AddressEntity::getId))
                .generate(field(AddressEntity::getMember), g -> g.oneOf(members))
                .create();

        return repo.saveAllAndFlush(entities);
    }
    

    private void verify(AddressDto expected, AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        verify((AddressCreateDto)expected, actual);
    }

    private void verify(AddressCreateDto expected, AddressEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressType().getAddressTypeName(),
            actual.getAddressType().getAddressTypeName());
    }

    private void verify(AddressEntity expected, AddressDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressType().getAddressTypeName(),
            actual.getAddressType().getAddressTypeName());
    }

    private void verify(AddressEntity expected, AddressFullNameDto actual) {
        assertEquals(expected.getMember().getFirstName(), actual.getFullName().getFirstName());
        assertEquals(expected.getMember().getMiddleName(), actual.getFullName().getMiddleName());
        assertEquals(expected.getMember().getLastName(), actual.getFullName().getLastName());
        assertEquals(expected.getMember().getSuffix(), actual.getFullName().getSuffix());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressType().getAddressTypeName(),
            actual.getAddressType().getAddressTypeName());
    }

    private void verify(List<AddressEntity> expected, List<AddressFullNameDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<AddressEntity, AddressFullNameDto>> getSorts() {

        Map<String, SortFields<AddressEntity, AddressFullNameDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(AddressEntity::getId),
            Comparator.comparingInt(AddressFullNameDto::getId)));

        map.put("address1", SortFields.of(Comparator.comparing(AddressEntity::getAddress1),
            Comparator.comparing(AddressFullNameDto::getAddress1)));

        map.put("address2", SortFields.of(Comparator.comparing(AddressEntity::getAddress2),
            Comparator.comparing(AddressFullNameDto::getAddress2)));

        map.put("city", SortFields.of(Comparator.comparing(AddressEntity::getCity),
            Comparator.comparing(AddressFullNameDto::getCity)));

        map.put("state", SortFields.of(Comparator.comparing(AddressEntity::getState),
            Comparator.comparing(AddressFullNameDto::getState)));

        map.put("postal_code", SortFields.of(Comparator.comparing(AddressEntity::getPostalCode),
            Comparator.comparing(AddressFullNameDto::getPostalCode)));

        map.put("address_type", SortFields.of(Comparator.comparing(AddressEntity::getAddressType),
            Comparator.comparing(AddressFullNameDto::getAddressType)));

        map.put("member_number", SortFields.of(
            Comparator.comparing(e -> e.getMember().getMemberNumber()),
            Comparator.comparing(AddressFullNameDto::getMemberNumber)));

        map.put("full_name", SortFields.of(
            Comparator.comparing(e -> e.getMember().getLastName()),
            Comparator.comparing(d -> d.getFullName().getLastName())));

        return map;
    }
}
