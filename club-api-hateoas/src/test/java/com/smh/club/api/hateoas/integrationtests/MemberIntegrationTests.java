package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.config.PagingConfig;
import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static java.util.Comparator.comparingInt;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class MemberIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    private final String listNodeName = "memberModelList";

    private final Map<String, SortFields<MemberEntity, MemberModel>> sorts;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/members");
        sorts = getSorts();
    }


    @ParameterizedTest
    @ValueSource(ints = /*{1, 5, 20,*/ {50})
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
        addEntitiesToDb(entitySize);

        // setup
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll().stream()
            .sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(PagingConfig.DIRECTION_NAME, "desc");

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
    public void getListPage_pageSize(int entitySize, int pageSize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

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
    public void getListPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(PagingConfig.PAGE_NAME, String.valueOf(page));

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberModel::getMemberNumber)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "member-number", "first-name", "last-name", "birth-date", "joined-date" })
    public void getListPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = sorts.get(sort);

        var sorted = memberRepo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(PagingConfig.SORT_NAME, sort);

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"middle-name", "suffix"})
    public void getListPage_excluded_sort_using_default(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = sorts.get(sort);

        var sorted = memberRepo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(PagingConfig.SORT_NAME, sort);

        var testParams = PageTestParams.of(MemberModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
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
    public void get_returns_status_notFound() throws Exception {
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
        var expected = Instancio.of(MemberModel.class)
            .ignore(field(MemberModel::getId))
            .create();

        // perform post
        var result =
            given()
                .auth().none()
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(expected))
                .when()
                    .post(path);

        var model = result.then().extract().body().as(MemberModel.class);
        var uri = "http://localhost" + path + "/" + model.getId();

        result.then()
                    .assertThat().status(HttpStatus.CREATED)
                    .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
                    .expect(jsonPath("$._links").exists())
                    .expect(jsonPath("$._links.length()").value(3))
                    .expect(jsonPath("$._links.self.href").value(uri))
                    .expect(jsonPath("$._links.update.href").value(uri))
                    .expect(jsonPath("$._links.delete.href").value(uri));

        var member = memberRepo.findById(model.getId());

        assertTrue(member.isPresent());
        verify(expected, member.get());
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var id = addEntitiesToDb(20).get(10).getId();
        var update = Instancio.of(MemberModel.class)
            .set(field(MemberModel::getId), id)
            .create();

        // perform put
        var uri = "http://localhost/api/v2/members/" + id;
        var actual =
            given()
                .auth().none()
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("id", id)
                .body(mapper.writeValueAsString(update))
            .when()
                .put( path + "/{id}")
            .then()
                .assertThat().status(HttpStatus.OK)
                .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
                .expect(jsonPath("$._links").exists())
                .expect(jsonPath("$._links.length()").value(3))
                .expect(jsonPath("$._links.self.href").value(uri))
                .expect(jsonPath("$._links.update.href").value(uri))
                .expect(jsonPath("$._links.delete.href").value(uri))
            .extract().body().as(MemberModel.class);

        var member = memberRepo.findById(actual.getId());
        assertTrue(member.isPresent());
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

    @Test
    public void delete_status_noContent() throws Exception {
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
    public void getCount_returns_count_status_ok() throws Exception {
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

        map.put("last-name", SortFields.of(Comparator.comparing(MemberEntity::getLastName),
            Comparator.comparing(MemberModel::getLastName)));

        map.put("birth-date", SortFields.of(Comparator.comparing(MemberEntity::getBirthDate),
            Comparator.comparing(MemberModel::getBirthDate)));

        map.put("joined-date", SortFields.of(Comparator.comparing(MemberEntity::getJoinedDate),
            Comparator.comparing(MemberModel::getJoinedDate)));

        return map;
    }
    
}
