package com.smh.club.api.hateoas.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests extends IntegrationTests {

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

    private final String listNodeName = "memberModelList";

    @WithSettings // Instancio settings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0)
                .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/members");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize)  {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll().stream()
            .sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();

        Map<String, String> map = new HashMap<>();
        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(MemberModel::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll().stream()
            .sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName, "member-number,desc");

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(MemberModel::getMemberNumber).reversed()).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, int pageSize) {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, pageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberModel::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            page, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberModel::getMemberNumber)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "member-number", "first-name", "last-name", "birth-date", "joined-date"})
    public void getPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = memberRepo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"middle-name", "suffix", "addresses", "emails", "phones", "renewals"})
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
        var member = addEntitiesToDb(20).get(10);
        var id = member.getId();

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
            .extract().body().as(MemberModel.class);

        verify(member, actual);
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
            .accept(MediaTypes.HAL_JSON)
        .when()
            .get( path + "/{id}")
        .then()
            .assertThat().status(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create_returns_model_status_created() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .create();

        // perform post
        var ret = sendValidCreate(create, MemberModel.class);

        var member = memberRepo.findById(ret.getId());

        assertTrue(member.isPresent());
        verify(create, member.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(MemberModel::getFirstName)),
            arguments(field(MemberModel::getLastName)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .setBlank(nonNullableField)
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .setBlank(field(MemberModel::getBirthDate))
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void create_with_null_joinedDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .setBlank(field(MemberModel::getJoinedDate))
            .create();

        sendInvalidCreate(create);
    }

    private static Stream<Arguments> nullableFields() {
        return Stream.of(
            arguments(field(MemberModel::getMiddleName)),
            arguments(field(MemberModel::getSuffix)));
    }

    @ParameterizedTest
    @MethodSource("nullableFields")
    public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .setBlank(nullableField)
            .create();

        // perform post
        var ret = sendValidCreate(create, MemberModel.class);

        var member = memberRepo.findById(ret.getId());

        assertTrue(member.isPresent());
        verify(create, member.get());
    }

    @Test
    public void create_with_invalid_memberNumber_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .set(field(MemberModel::getMemberNumber), 0)
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_birthDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .set(field(MemberModel::getBirthDate), LocalDate.now())
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_joinedDate_returns_bad_request() throws Exception {
        // setup
        var create = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .set(field(MemberModel::getBirthDate), LocalDate.now().minusYears(22))
            .set(field(MemberModel::getJoinedDate), LocalDate.now().minusYears(10))
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .create();

        // perform put
        var actual = sendValidUpdate(id, update, MemberModel.class);

        var member = memberRepo.findById(actual.getId());
        assertTrue(member.isPresent());
        assertEquals(update, actual);
        verify(member.get(), actual);
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(MemberModel.class);

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
    public void update_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .setBlank(nonNullableField)
            .create();

        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_null_birthDate_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .setBlank(field(MemberModel::getBirthDate))
            .create();

        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_birthDate_returns_bad_request() throws Exception {
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .set(field(MemberModel::getJoinedDate), LocalDate.now())
            .set(field(MemberModel::getBirthDate), LocalDate.now().minusYears(10))
            .create();

        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_joinedDate_returns_bad_request() throws Exception {
        var entity = addEntitiesToDb(5).get(2);
        var id = entity.getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .set(field(MemberModel::getBirthDate), LocalDate.now().minusYears(22))
            .generate(field(MemberModel::getJoinedDate),
                g -> g.temporal().localDate().min(LocalDate.now().minusYears(10)))
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

        var member = memberRepo.findById(id);
        assertTrue(member.isEmpty());
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
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();

        return memberRepo.saveAllAndFlush(entities);
    }

    private void verify(MemberModel expected, MemberEntity actual) {
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(MemberEntity expected, MemberModel actual) {
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(List<MemberEntity> expected, List<MemberModel> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<MemberEntity, MemberModel>> getSorts() {

        Map<String, SortFields<MemberEntity, MemberModel>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(MemberEntity::getId),
                                    Comparator.comparingInt(MemberModel::getId)));

        map.put("member-number", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberModel::getMemberNumber)));

        map.put("first-name", SortFields.of(Comparator.comparing(MemberEntity::getFirstName),
            Comparator.comparing(MemberModel::getFirstName)));

        map.put("middle-name", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberModel::getMemberNumber)));

        map.put("last-name", SortFields.of(Comparator.comparing(MemberEntity::getLastName),
            Comparator.comparing(MemberModel::getLastName)));

        map.put("suffix", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberModel::getMemberNumber)));

        map.put("birth-date", SortFields.of(Comparator.comparing(MemberEntity::getBirthDate),
            Comparator.comparing(MemberModel::getBirthDate)));

        map.put("joined-date", SortFields.of(Comparator.comparing(MemberEntity::getJoinedDate),
            Comparator.comparing(MemberModel::getJoinedDate)));

        return map;
    }
    
}
