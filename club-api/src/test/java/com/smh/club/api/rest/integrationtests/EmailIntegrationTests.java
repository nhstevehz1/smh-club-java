package com.smh.club.api.rest.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.repos.EmailRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.rest.dto.AddressDto;
import com.smh.club.api.rest.dto.EmailDto;
import com.smh.club.api.rest.response.CountResponse;
import io.restassured.http.ContentType;
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
import org.junit.jupiter.params.provider.MethodSource;
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
import static org.junit.jupiter.params.provider.Arguments.arguments;
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
    private MembersRepo memberRepo;

    @Autowired
    private EmailRepo repo;

    @WithSettings // Instancio settings
    Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Autowired
    public EmailIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v1/emails");
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
    public void getListPage_no_params(int entitySize)  {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        var testParams = PageTestParams.of(EmailDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId).reversed()).toList();
        Map<String, String> map = new HashMap<>();
        map.put(sortParamName,  "id,desc");

        var testParams = PageTestParams.of(EmailDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(sizeParamName, String.valueOf(pageSize));

        var testParams = PageTestParams.of(EmailDto.class, map, path, sorted.size(),
            0, pageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8})
    public void getListPage_page(int page) {
        addEntitiesToDb(150);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        Map<String,String> map = new HashMap<>();
        map.put(pageParamName, String.valueOf(page));

        var testParams = PageTestParams.of(EmailDto.class, map, path, sorted.size(),
            page, defaultPageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "email", "email-type" })
    public void getListPage_sortColumn(String sort) {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();

        var map = new HashMap<String, String>();
        map.put(sortParamName, sort);

        var testParams = PageTestParams.of(EmailDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(sortFields.getDto()).toList(), actual);

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
                .extract().body().as(EmailDto.class);

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
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get( path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create_returns_dto_status_created() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailDto.class)
            .generate(field(EmailDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore((field(EmailDto::getId)))
            .create();

        // perform POST
        var ret =
            given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(create))
            .when()
                .post(path)
            .then()
                .assertThat().status(HttpStatus.CREATED)
                .extract().body().as(AddressDto.class);

        // verify
        var entity =  repo.findById(ret.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    // used with test that follows
    private static Stream<Arguments> nonNullableFields() {
        return Stream.of(
            arguments(field(EmailDto::getEmail)),
            arguments(field(EmailDto::getEmailType)));
    }

    @ParameterizedTest
    @MethodSource("nonNullableFields")
    public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailDto.class)
            .generate(field(EmailDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore(field(EmailDto::getId))
            .ignore(nonNullableField)
            .create();

        // perform POST
        given()
            .auth().none()
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(create))
            .when()
            .post(path)
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .assertThat().contentType(ContentType.JSON)
            .expect(jsonPath("$.validation-errors").isNotEmpty());
    }

    @Test
    public void create_with_invalid_email_returns_bad_request() throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailDto.class)
            .generate(field(EmailDto::getMemberId), g -> g.oneOf(memberIdList))
            .ignore(field(EmailDto::getId))
            .set(field(EmailDto::getEmail), "X")
            .create();

        // perform POST
        given()
            .auth().none()
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(create))
            .when()
            .post(path)
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .assertThat().contentType(ContentType.JSON)
            .expect(jsonPath("$.validation-errors").isNotEmpty())
            .expect(jsonPath("$.validation-errors.length()").value(1));
    }

    @Test
    public void update_returns_dto_status_ok() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailDto.class)
            .set(field(EmailDto::getId), id)
            .set(field(EmailDto::getMemberId), memberId)
            .create();

        // perform PUT
        var actual =
            given()
                .auth().none()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("id", id)
                .body(mapper.writeValueAsString(update))
            .when()
                .put(path + "/{id}")
            .then()
                .assertThat().status(HttpStatus.OK)
                .assertThat().contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(EmailDto.class);

        // verify
        var email = repo.findById(actual.getId());

        assertTrue(email.isPresent());
        verify(update, email.get());
    }

    @Test
    public void update_returns_status_bad_request() throws Exception {
        // Setup
        var update = Instancio.create(EmailDto.class);

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
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailDto.class)
            .set(field(EmailDto::getId), id)
            .set(field(EmailDto::getMemberId), memberId)
            .ignore(nonNullableField)
            .create();

        given()
            .auth().none()
            .accept(MediaType.APPLICATION_JSON)
            .pathParam("id", update.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
            .when()
            .put(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .assertThat().contentType(ContentType.JSON)
            .expect(jsonPath("$.validation-errors").isNotEmpty())
            .expect(jsonPath("$.validation-errors.length()").value(1));
    }

    @Test
    public void update_with_invalid_email_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailDto.class)
            .set(field(EmailDto::getId), id)
            .set(field(EmailDto::getMemberId), memberId)
            .set(field(EmailDto::getEmail), "XXX")
            .create();

        given()
            .auth().none()
            .accept(MediaType.APPLICATION_JSON)
            .pathParam("id", update.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
            .when()
            .put(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .assertThat().contentType(ContentType.JSON)
            .expect(jsonPath("$.validation-errors").isNotEmpty())
            .expect(jsonPath("$.validation-errors.length()").value(1));
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

    private void verify(EmailDto expected, EmailEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(EmailEntity expected, EmailDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(List<EmailEntity> expected, List<EmailDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<EmailEntity, EmailDto>> getSorts() {

        Map<String, SortFields<EmailEntity, EmailDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(EmailEntity::getId),
            Comparator.comparingInt(EmailDto::getId)));

        map.put("member-id", SortFields.of(Comparator.comparingInt(EmailEntity::getId),
            Comparator.comparingInt(EmailDto::getId)));

        map.put("email", SortFields.of(Comparator.comparing(EmailEntity::getEmail),
            Comparator.comparing(EmailDto::getEmail)));

        map.put("email-type", SortFields.of(Comparator.comparing(EmailEntity::getEmailType),
            Comparator.comparing(EmailDto::getEmailType)));

        return map;
    }

}
