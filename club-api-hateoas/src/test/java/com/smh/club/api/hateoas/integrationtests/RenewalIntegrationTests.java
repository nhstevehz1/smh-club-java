package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.RenewalsRepo;
import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
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
import smh.club.shared.config.PagingConfig;

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
public class RenewalIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private RenewalsRepo repo;

    @Autowired
    private MembersRepo memberRepo;

    private List<MemberEntity> members;

    private final String listNodeName = "renewalModelList";

    @WithSettings
    private final Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    public RenewalIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v2/renewals");
    }

    @BeforeEach
    public void init() {

        var members = Instancio.ofList(MemberEntity.class)
            .size(5)
            .ignore(field(MemberEntity::getId))
            .withUnique(field(MemberEntity::getMemberNumber))
            .create();

        this.members = memberRepo.saveAllAndFlush(members);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize)  {
        // setup
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
            .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        Map<String, String> map = new HashMap<>();
        var testParams = PageTestParams.of(RenewalModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(RenewalModel::getId)).toList(), actual);

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
            .sorted(Comparator.comparingInt(RenewalEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(PagingConfig.DIRECTION_NAME, "desc");

        var testParams = PageTestParams.of(RenewalModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(RenewalModel::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(testParams.getPageSize()).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, int pageSize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

        var testParams = PageTestParams.of(RenewalModel.class, map, path, sorted.size(),
            0, pageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(RenewalModel::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getListPage_page(int page) {
        var entitySize = 100;
        addEntitiesToDb(entitySize);
        var sorted = repo.findAll()
            .stream().sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());

        Map<String,String> map = new HashMap<>();
        map.put(PagingConfig.PAGE_NAME, String.valueOf(page));

        var testParams = PageTestParams.of(RenewalModel.class, map, path, sorted.size(),
            page, defaultPageSize, listNodeName);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
            .sorted(Comparator.comparingInt(RenewalModel::getId)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "member-id", "renewal-date", "renewal-year" })
    public void getListPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        var map = new HashMap<String, String>();
        map.put(PagingConfig.SORT_NAME, sort);

        var testParams = PageTestParams.of(RenewalModel.class, map, path, sorted.size(),
            0, defaultPageSize, listNodeName);
        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getModel()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @Test
    public void get_returns_model_status_ok() {
        // setup
        var renewal = addEntitiesToDb(20).get(10);
        var id = renewal.getId();

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
                .extract().body().as(RenewalModel.class);

        verify(renewal, actual);
    }

    @Test
    public void get_returns_status_notFound() {
        // Setup
        var entities = addEntitiesToDb(5);
        var highest = entities.stream().max(comparingInt(RenewalEntity::getId)).map(RenewalEntity::getId).orElseThrow();
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
        var id = memberRepo.findAll().get(2).getId();

        var expected = Instancio.of(RenewalModel.class)
            .ignore(field(RenewalModel::getId))
            .set(field(RenewalModel::getMemberId), id)
            .generate(field(RenewalModel::getRenewalYear),
                g-> g.text().pattern("#d#d#d#d"))
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

        var model = result.then().extract().body().as(RenewalModel.class);
        var uri = "http://localhost" + path + "/" + model.getId();

        result.then()
            .assertThat().status(HttpStatus.CREATED)
            .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
            .expect(jsonPath("$._links").exists())
            .expect(jsonPath("$._links.length()").value(3))
            .expect(jsonPath("$._links.self.href").value(uri))
            .expect(jsonPath("$._links.update.href").value(uri))
            .expect(jsonPath("$._links.delete.href").value(uri));

        var renewal = repo.findById(model.getId());

        assertTrue(renewal.isPresent());
        verify(expected, renewal.get());
    }

    @Test
    public void update_returns_model_status_ok() throws Exception{
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(RenewalModel.class)
            .set(field(RenewalModel::getId), id)
            .set(field(RenewalModel::getMemberId), memberId)
            .generate(field(RenewalModel::getRenewalYear),
                g-> g.text().pattern("#d#d#d#d"))
            .create();

        // perform put
        var uri = "http://localhost/api/v2/renewals/" + id;
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
                .extract().body().as(RenewalModel.class);

        var renewal = repo.findById(actual.getId());
        assertTrue(renewal.isPresent());
        verify(renewal.get(), actual);
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(RenewalModel.class);

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

        var renewal = repo.findById(id);
        assertTrue(renewal.isEmpty());
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

    private List<RenewalEntity> addEntitiesToDb(int size) {
        var entities = Instancio.ofList(RenewalEntity.class)
            .size(size)
            .ignore(field(RenewalEntity::getId))
            .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
            .generate(field(RenewalEntity::getRenewalYear),
                g-> g.text().pattern("#d#d#d#d"))
            .create();

        return repo.saveAllAndFlush(entities);
    }


    private void verify(RenewalModel expected, RenewalEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(RenewalEntity expected, RenewalModel actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }


    private void verify(List<RenewalEntity> expected, List<RenewalModel> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<RenewalEntity, RenewalModel>> getSorts() {

        Map<String, SortFields<RenewalEntity, RenewalModel>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(RenewalEntity::getId),
            Comparator.comparingInt(RenewalModel::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(RenewalEntity::getId),
            Comparator.comparingInt(RenewalModel::getId)));

        map.put("renewal-date", SortFields.of(Comparator.comparing(RenewalEntity::getRenewalDate),
            Comparator.comparing(RenewalModel::getRenewalDate)));

        map.put("renewal-year", SortFields.of(Comparator.comparing(RenewalEntity::getRenewalYear),
            Comparator.comparing(RenewalModel::getRenewalYear)));

        return map;
    }
}
