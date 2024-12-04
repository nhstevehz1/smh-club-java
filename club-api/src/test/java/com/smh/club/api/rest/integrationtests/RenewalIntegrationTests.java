package com.smh.club.api.rest.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.response.CountResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class RenewalIntegrationTests extends IntegrationTests {

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
    private RenewalsRepo repo;

    @WithSettings // Instancio settings
    Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    public RenewalIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v1/renewals");
    }

    @BeforeEach
    public void init() {
        // there seems to be a bug where @WithSettings is not recognized in before all
        var members = Instancio.ofList(MemberEntity.class)
                .size(5)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();

        memberRepo.saveAllAndFlush(members);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        var testParams = PageTestParams.of(RenewalDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId).reversed()).toList();

        Map<String, String> map = new HashMap<>();
        map.put(sortParamName,  "id,desc");

        var testParams = PageTestParams.of(RenewalDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(RenewalDto.class, map, path, sorted.size(),
            0, pageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    public void getListPage_page(int page) {
        addEntitiesToDb(150);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(RenewalDto.class, map, path, sorted.size(),
            page, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "renewal-date", "renewal-year" })
    public void getListPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(RenewalDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id"})
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
                .extract().body().as(RenewalDto.class);

        verify(email, actual);
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
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get( path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1})
    public void create_returns_dto_status_created(int offset) throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(RenewalDto.class)
            .generate(field(RenewalDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore((field(RenewalDto::getId)))
            .set(field(RenewalDto::getRenewalDate), LocalDate.now())
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear() - offset)
            .create();

        // perform POST
        var ret = sendValidCreate(create, RenewalDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void create_with_null_renewalDate_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(RenewalDto.class)
            .generate(field(RenewalDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore((field(RenewalDto::getId)))
            .setBlank(field(RenewalDto::getRenewalDate))
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_renewalDate_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(RenewalDto.class)
            .generate(field(RenewalDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore((field(RenewalDto::getId)))
            .set(field(RenewalDto::getRenewalDate), LocalDate.now().plusYears(1))
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear())
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_renewalYear_returns_bad_request() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(RenewalDto.class)
            .generate(field(RenewalDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore((field(RenewalDto::getId)))
            .set(field(RenewalDto::getRenewalDate), LocalDate.now())
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear() + 1)
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1})
    public void update_returns_dto_status_ok(int offset) throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(RenewalDto.class)
            .set(field(RenewalDto::getId), id)
            .set(field(RenewalDto::getMemberId), memberId)
            .set(field(RenewalDto::getRenewalDate), LocalDate.now())
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear() - offset)
            .create();

        // perform PUT
        var actual = sendValidUpdate(id, update, RenewalDto.class);

        // verify
        var email = repo.findById(actual.getId());

        assertTrue(email.isPresent());
        verify(update, email.get());
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var update = Instancio.create(RenewalDto.class);

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

    @Test
    public void update_with_null_renewalDate_returns_badRequest() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(RenewalDto.class)
            .set(field(RenewalDto::getId), id)
            .set(field(RenewalDto::getMemberId), memberId)
            .setBlank(field(RenewalDto::getRenewalDate))
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear())
            .create();

        // perform put
        sendInvalidUpdate(id, update);

    }

    @Test
    public void update_with_invalid_renewalDate_returns_badRequest() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(RenewalDto.class)
            .set(field(RenewalDto::getId), id)
            .set(field(RenewalDto::getMemberId), memberId)
            .setBlank(field(RenewalDto::getRenewalDate))
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear())
            .create();

        // perform put
        sendInvalidUpdate(id, update);

    }

    @Test
    public void update_with_invalid_renewalYear_returns_badRequest() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(RenewalDto.class)
            .set(field(RenewalDto::getId), id)
            .set(field(RenewalDto::getMemberId), memberId)
            .set(field(RenewalDto::getRenewalDate), LocalDate.now())
            .set(field(RenewalDto::getRenewalYear), LocalDate.now().getYear() + 1)
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

    private List<RenewalEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(RenewalEntity.class)
                .size(size)
                .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
                .ignore(field(RenewalEntity::getId))
                .create();

        return repo.saveAllAndFlush(entities);
    }

    private void verify(RenewalDto expected, RenewalEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(RenewalEntity expected, RenewalDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(List<RenewalEntity> expected, List<RenewalDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<RenewalEntity, RenewalDto>> getSorts() {

        Map<String, SortFields<RenewalEntity, RenewalDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(RenewalEntity::getId),
            Comparator.comparingInt(RenewalDto::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(RenewalEntity::getId),
            Comparator.comparingInt(RenewalDto::getId)));

        map.put("renewal-date", SortFields.of(Comparator.comparing(RenewalEntity::getRenewalDate),
            Comparator.comparing(RenewalDto::getRenewalDate)));

        map.put("renewal-year", SortFields.of(Comparator.comparing(RenewalEntity::getRenewalYear),
            Comparator.comparing(RenewalDto::getRenewalYear)));

        return map;
    }
}
