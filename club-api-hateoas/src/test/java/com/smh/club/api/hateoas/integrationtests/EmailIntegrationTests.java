package com.smh.club.api.hateoas.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.repos.EmailRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.hateoas.models.EmailModel;
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
public class EmailIntegrationTests extends IntegrationTests {

    @Value("${spring.data.web.pageable.default-page-size:20}")
    private int defaultPageSize;

    @Value("${spring.data.rest.sort-param-name:sort}")
    private String sortParamName;

    @Value("${spring.data.rest.size-param-name:size}")
    private String sizeParamName;

    @Value("${spring.data.rest.page-param-name:page}")
    private String pageParamName;

    @Autowired
    private EmailRepo repo;

    @Autowired
    private MembersRepo memberRepo;

     private final String listNodeName = "emailModelList";

    @WithSettings // Instancio settings
    private final Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public EmailIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/emails");
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
            .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        Map<String, String> map = new HashMap<>();
        var testParams = PageTestParams.of(EmailModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(EmailModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        addEntitiesToDb(entitySize);

        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(EmailEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName, "id,desc");

        var testParams = PageTestParams.of(EmailModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(EmailModel::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, int pageSize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(EmailEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(EmailModel.class, map, path, sorted.size(),
            0, pageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(EmailModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(EmailEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(EmailModel.class, map, path, sorted.size(),
            page, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(EmailModel::getId)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "email", "email-type" })
    public void getPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(EmailModel.class, map, path, sorted.size(),
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
        var email = addEntitiesToDb(20).get(10);
        var id = email.getId();

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
                .extract().body().as(EmailModel.class);

        verify(email, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        // Setup
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(EmailEntity::getId)).map(EmailEntity::getId).orElseThrow();
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
        var create = Instancio.of(EmailModel.class)
            .ignore(field(EmailModel::getId))
            .generate(field(EmailModel::getMemberId), g -> g.oneOf(memberIdList))
            .create();

        // perform post
        var ret = sendValidCreate(create, EmailModel.class);

        var entity = repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(EmailModel::getEmail)),
            arguments(field(EmailModel::getEmailType)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailModel.class)
            .ignore(field(EmailModel::getId))
            .generate(field(EmailModel::getMemberId), g -> g.oneOf(memberIdList))
            .setBlank(nonNullableField)
            .create();

        sendInvalidCreate(create);

    }

    @Test
    public void create_with_invalid_email_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailModel.class)
            .ignore(field(EmailModel::getId))
            .generate(field(EmailModel::getMemberId), g -> g.oneOf(memberIdList))
            .set(field(EmailModel::getEmail), "XX")
            .create();

        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailModel.class)
            .set(field(EmailModel::getId), id)
            .set(field(EmailModel::getMemberId), memberId)
            .create();

        // perform put
        var ret = sendValidUpdate(id, update, EmailModel.class);

        var email = repo.findById(ret.getId());

        assertTrue(email.isPresent());
        verify(update, email.get());
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(EmailModel.class);

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
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailModel.class)
            .set(field(EmailModel::getId), id)
            .set(field(EmailModel::getMemberId), memberId)
            .setBlank(nonNullableField)
            .create();

        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_email_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailModel.class)
            .set(field(EmailModel::getId), id)
            .set(field(EmailModel::getMemberId), memberId)
            .set(field(EmailModel::getEmail), "XX")
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

    private List<EmailEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(EmailEntity.class)
            .size(size)
            .ignore(field(EmailEntity::getId))
            .generate(field(EmailEntity::getMember), g -> g.oneOf(members))
            .create();

        return repo.saveAllAndFlush(entities);
    }
    
    private void verify(EmailModel expected, EmailEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(EmailEntity expected, EmailModel actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }
    
    private void verify(List<EmailEntity> expected, List<EmailModel> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<EmailEntity, EmailModel>> getSorts() {

        Map<String, SortFields<EmailEntity, EmailModel>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(EmailEntity::getId),
            Comparator.comparingInt(EmailModel::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(EmailEntity::getId),
            Comparator.comparingInt(EmailModel::getId)));

        map.put("email", SortFields.of(Comparator.comparing(EmailEntity::getEmail),
            Comparator.comparing(EmailModel::getEmail)));

        map.put("email-type", SortFields.of(Comparator.comparing(EmailEntity::getEmailType),
            Comparator.comparing(EmailModel::getEmailType)));

        return map;
    }
}
