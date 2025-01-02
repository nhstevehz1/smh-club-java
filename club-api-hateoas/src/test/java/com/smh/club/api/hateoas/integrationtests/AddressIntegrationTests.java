package com.smh.club.api.hateoas.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.hateoas.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.domain.repos.AddressRepo;
import com.smh.club.api.hateoas.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@ExtendWith(SpringExtension.class)
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AddressIntegrationTests extends IntegrationTests {

    @Value("${spring.data.web.pageable.default-page-size:20}")
    private int defaultPageSize;

    @Value("${spring.data.rest.sort-param-name:sort}")
    private String sortParamName;

    @Value("${spring.data.rest.size-param-name:size}")
    private String sizeParamName;

    @Value("${spring.data.rest.page-param-name:page}")
    private String pageParamName;

    @Autowired
    private AddressRepo repo;

    @Autowired
    private MembersRepo memberRepo;

    private final String listNodeName = "addressModelList";

    @WithSettings // Instancio settings
    private final Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public AddressIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/addresses");
    }

    @BeforeEach
    public void init() {

        var members = Instancio.ofList(MemberEntity.class)
            .size(5)
            .ignore(field(MemberEntity::getId))
            .withUnique(field(MemberEntity::getMemberNumber))
            .create();

        memberRepo.saveAllAndFlush(members);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize)  {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        Map<String, String> map = new HashMap<>();
        var testParams = PageTestParams.of(AddressModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(AddressModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(AddressEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName,  "id,desc");

        var testParams = PageTestParams.of(AddressModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(AddressModel::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, int pageSize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(AddressEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(AddressModel.class, map, path, sorted.size(),
            0, pageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(AddressModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getListPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(AddressEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(AddressModel.class, map, path, sorted.size(),
            page, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(AddressModel::getId)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "address1", "city", "state", "zip", "address-type" })
    public void getListPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(AddressModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id", "address2"})
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
    public void get_returns_model_status_ok() {
        // setup
        var address = addEntitiesToDb(20).get(10);
        var id = address.getId();

        // perform get
        var uri = "http://localhost" + path + "/" + id;
        var actual =
            given()
                .auth().none()
                .pathParam("id", id)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .when()
                .get( path + "/{id}")
                .then()
                .assertThat().status(HttpStatus.OK)
                .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
                .expect(jsonPath("$._links").exists())
                .expect(jsonPath("$._links.length()").value(3))
                .expect(jsonPath("$._links.self.href").value(uri))
                .expect(jsonPath("$._links.update.href").value(uri))
                .expect(jsonPath("$._links.delete.href").value(uri))
                .extract().body().as(AddressModel.class);

        verify(address, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        // Setup
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(AddressEntity::getId)).map(AddressEntity::getId).orElseThrow();
        var id = highest + 100;

        // perform get and verify
        given()
            .auth().none()
            .pathParam("id", id)
            .accept(MediaTypes.HAL_JSON)
            .when()
            .get( path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d#d#d#d#d", "#d#d#d#d#d-#d#d#d#d"})
    public void create_returns_model_status_created(String pattern) throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressModel.class)
            .ignore(field(AddressModel::getId))
            .generate(field(AddressModel::getMemberId),
            g -> g.oneOf(memberIdList))
            .generate(field(AddressModel::getZip), g -> g.text().pattern(pattern))
            .create();

        // perform post
        var actual = sendValidCreate(create, AddressModel.class);

        var address = repo.findById(actual.getId());

        assertTrue(address.isPresent());
        verify(create, address.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullFields() {
        return Stream.of(
            arguments(field(AddressModel::getAddress1)),
            arguments(field(AddressModel::getCity)),
            arguments(field(AddressModel::getState)),
            arguments(field(AddressModel::getState)));
    }

    @ParameterizedTest
    @MethodSource("nonNullFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception{
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressModel.class)
            .ignore(field(AddressModel::getId))
            .generate(field(AddressModel::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(nonNullableField)
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_zip_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressModel.class)
            .ignore(field(AddressModel::getId))
            .generate(field(AddressModel::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(field(AddressModel::getZip))
            .create();

        sendInvalidCreate(create);
    }

    private static Stream<Arguments> nullableFields() {
        return Stream.of(
            arguments(field(AddressModel::getAddress2)));
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressModel.class)
            .ignore(field(AddressModel::getId))
            .generate(field(AddressModel::getMemberId),
                g -> g.oneOf(memberIdList))
            .setBlank(nullableField)
            .create();

        var actual = sendValidCreate(create, AddressModel.class);

        var address = repo.findById(actual.getId());

        assertTrue(address.isPresent());
        verify(create, address.get());
    }

    @Test
    public void create_with_invalid_zip_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(AddressModel.class)
            .ignore(field(AddressModel::getId))
            .generate(field(AddressModel::getMemberId),
                g -> g.oneOf(memberIdList))
            .set(field(AddressModel::getZip), "AAA")
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var update = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), memberId)
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, AddressModel.class);

        var address = repo.findById(actual.getId());
        assertTrue(address.isPresent());
        verify(address.get(), actual);
    }

    @ParameterizedTest
    @MethodSource("nonNullFields")
    public void update_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var update = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), memberId)
            .setBlank(nonNullableField)
            .create();

        sendInvalidUpdate(id, update);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d","#d#d","#d#d#d","#d#d#d#d", "#d#d#d#d#d-#d#d#d", "#c#c"})
    public void update_with_invalid_zip_returns_bad_request(String pattern) throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var update = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), memberId)
            .generate(field(AddressModel::getZip), g -> g.text().pattern(pattern))
            .create();

        sendInvalidUpdate(id, update);
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void update_nullableField_returns_dto_status_ok(Selector nullableField) throws JsonProcessingException {
// setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var update = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), memberId)
            .setBlank(nullableField)
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, AddressModel.class);

        var address = repo.findById(actual.getId());
        assertTrue(address.isPresent());
        verify(address.get(), actual);
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(AddressModel.class);

        // perform put
        given()
            .auth().none()
            .accept(MediaTypes.HAL_JSON)
            .pathParam("id", update.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
            .when()
            .put(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST);

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

        var address = repo.findById(id);
        assertTrue(address.isEmpty());
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

    private List<AddressEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(AddressEntity.class)
            .size(size)
            .ignore(field(AddressEntity::getId))
            .generate(field(AddressEntity::getMember), g -> g.oneOf(members))
            .create();

        return repo.saveAllAndFlush(entities);
    }

    private void verify(AddressModel expected, AddressEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getZip(), actual.getZip());
        assertEquals(expected.getAddressType(), actual.getAddressType());
    }

    private void verify(AddressEntity expected, AddressModel actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getZip(), actual.getZip());
        assertEquals(expected.getAddressType(), actual.getAddressType());
    }

    private void verify(List<AddressEntity> expected, List<AddressModel> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<AddressEntity, AddressModel>> getSorts() {

        Map<String, SortFields<AddressEntity, AddressModel>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(AddressEntity::getId),
            Comparator.comparingInt(AddressModel::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(AddressEntity::getId),
            Comparator.comparingInt(AddressModel::getId)));

        map.put("address1", SortFields.of(Comparator.comparing(AddressEntity::getAddress1),
            Comparator.comparing(AddressModel::getAddress1)));

        map.put("address2", SortFields.of(Comparator.comparing(AddressEntity::getId),
            Comparator.comparing(AddressModel::getId)));

        map.put("city", SortFields.of(Comparator.comparing(AddressEntity::getCity),
            Comparator.comparing(AddressModel::getCity)));

        map.put("state", SortFields.of(Comparator.comparing(AddressEntity::getState),
            Comparator.comparing(AddressModel::getState)));

        map.put("zip", SortFields.of(Comparator.comparing(AddressEntity::getZip),
            Comparator.comparing(AddressModel::getZip)));

        map.put("address-type", SortFields.of(Comparator.comparing(AddressEntity::getAddressType),
            Comparator.comparing(AddressModel::getAddressType)));

        return map;
    }

}
