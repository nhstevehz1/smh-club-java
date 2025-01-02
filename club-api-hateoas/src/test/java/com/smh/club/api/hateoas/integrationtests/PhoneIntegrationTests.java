package com.smh.club.api.hateoas.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.domain.repos.PhoneRepo;
import com.smh.club.api.hateoas.models.PhoneModel;
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
public class PhoneIntegrationTests extends IntegrationTests {

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
    private PhoneRepo repo;

    private final String listNodeName = "phoneModelList";

    @WithSettings // Instancio settings
    private final Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public PhoneIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/phones");
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
    public void getPage_no_params(int entitySize)  {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();

        Map<String, String> map = new HashMap<>();
        var testParams = PageTestParams.of(PhoneModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(PhoneModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getPage_sortDir_desc(int entitySize) {
        addEntitiesToDb(entitySize);

        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(PhoneEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName, "id,desc");

        var testParams = PageTestParams.of(PhoneModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(PhoneModel::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getPage_pageSize(int entitySize, int pageSize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(PhoneModel.class, map, path, sorted.size(),
            0, pageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(PhoneModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(PhoneModel.class, map, path, sorted.size(),
            page, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(PhoneModel::getId)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "phone-number", "phone-type" })
    public void getPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(PhoneModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id"})
    public void getPage_excluded_fields_returns_bad_request(String sort) {
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
        var phone = addEntitiesToDb(20).get(10);
        var id = phone.getId();

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
                .extract().body().as(PhoneModel.class);

        verify(phone, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        // Setup
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(PhoneEntity::getId)).map(PhoneEntity::getId).orElseThrow();
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

    @Test
    public void create_returns_model_status_created() throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(PhoneModel.class)
            .generate(field(PhoneModel::getMemberId), g -> g.oneOf(memberIdList))
            .ignore(field(PhoneModel::getId))
            .create();

        // perform post
        var ret = sendValidCreate(create, PhoneModel.class);

        var phone = repo.findById(ret.getId());

        assertTrue(phone.isPresent());
        verify(create, phone.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(PhoneModel::getPhoneNumber)),
            arguments(field(PhoneModel::getPhoneType)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(PhoneModel.class)
            .generate(field(PhoneModel::getMemberId), g -> g.oneOf(memberIdList))
            .ignore(field(PhoneModel::getId))
            .setBlank(nonNullableField)
            .create();

        sendInvalidCreate(create);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d#d#d", "#c#c#c#c#c#c#c#c#c#c"})
    public void create_with_invalid_phoneNumber_returns_bad_request(String pattern) throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(PhoneModel.class)
            .generate(field(PhoneModel::getMemberId), g -> g.oneOf(memberIdList))
            .ignore(field(PhoneModel::getId))
            .generate(field(PhoneModel::getPhoneNumber), g -> g.text().pattern(pattern))
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(PhoneModel.class)
            .set(field(PhoneModel::getId), id)
            .set(field(PhoneModel::getMemberId), memberId)
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, PhoneModel.class);

        var phone = repo.findById(actual.getId());

        assertTrue(phone.isPresent());
        verify(phone.get(), actual);
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(PhoneModel.class);

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

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void update_with_nonNullable_fields_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(PhoneModel.class)
            .set(field(PhoneModel::getId), id)
            .set(field(PhoneModel::getMemberId), memberId)
            .setBlank(nonNullableField)
            .create();

        sendInvalidUpdate(id, update);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#d#d#d", "#c#c#c#c#c#c#c#c#c#c"})
    public void update_with_invalid_phoneNumber_returns_bad_request(String pattern) throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(PhoneModel.class)
            .set(field(PhoneModel::getId), id)
            .set(field(PhoneModel::getMemberId), memberId)
            .generate(field(PhoneModel::getPhoneNumber), g -> g.text().pattern(pattern))
            .create();

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

        var phone = repo.findById(id);
        assertTrue(phone.isEmpty());
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

    private List<PhoneEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(PhoneEntity.class)
            .size(size)
            .ignore(field(PhoneEntity::getId))
            .generate(field(PhoneEntity::getMember), g -> g.oneOf(members))
            .create();

        return repo.saveAllAndFlush(entities);
    }


    private void verify(PhoneModel expected, PhoneEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getPhoneType(), actual.getPhoneType());
    }

    private void verify(PhoneEntity expected, PhoneModel actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getPhoneType(), actual.getPhoneType());
    }


    private void verify(List<PhoneEntity> expected, List<PhoneModel> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<PhoneEntity, PhoneModel>> getSorts() {

        Map<String, SortFields<PhoneEntity, PhoneModel>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(PhoneEntity::getId),
            Comparator.comparingInt(PhoneModel::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(PhoneEntity::getId),
            Comparator.comparingInt(PhoneModel::getId)));

        map.put("phone-number", SortFields.of(Comparator.comparing(PhoneEntity::getPhoneNumber),
            Comparator.comparing(PhoneModel::getPhoneNumber)));

        map.put("phone-type", SortFields.of(Comparator.comparing(PhoneEntity::getPhoneType),
            Comparator.comparing(PhoneModel::getPhoneType)));

        return map;
    }
}
