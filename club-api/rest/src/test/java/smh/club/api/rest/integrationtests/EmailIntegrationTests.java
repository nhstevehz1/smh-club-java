package com.smh.club.api.rest.integrationtests;

import static java.util.Comparator.comparingInt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.EmailRepo;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.email.*;
import com.smh.club.api.rest.response.CountResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"ROLE_club-admin", "ROLE_club-user"})
@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class EmailIntegrationTests extends IntegrationTests {

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
                .size(100)
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
        var testParams = PageTestParams.of(EmailFullNameDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(EmailFullNameDto::getId)).toList(), actual);

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

        var testParams = PageTestParams.of(EmailFullNameDto.class, map, path, sorted.size(),
            0, defaultPageSize);

        var actual = executeListPage(testParams);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailFullNameDto::getId).reversed()).toList(), actual);

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

        var testParams = PageTestParams.of(EmailFullNameDto.class, map, path, sorted.size(),
            0, pageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailFullNameDto::getId)).toList(), actual);

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

        var testParams = PageTestParams.of(EmailFullNameDto.class, map, path, sorted.size(),
            page, defaultPageSize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailFullNameDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"id,id", "email,email", "email_type,emailType",
        "member_number,member.memberNumber", "full_name,member.lastName" })
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

        var testParams = PageTestParams.of(EmailFullNameDto.class, map, path, expected.getTotalElements(),
            0, entitySize);

        var actual = executeListPage(testParams);
        assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

        for(int ii = 0; ii < entitySize; ii++) {
            verify(expected.getContent().get(ii), actual.get(ii));
        }
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
        var create = Instancio.of(EmailCreateDto.class)
            .generate(field(EmailCreateDto::getMemberId), g -> g.oneOf(memberIdList))
            .create();

        // perform POST
        var actual = sendValidCreate(create, EmailDto.class);

        // verify
        var entity =  repo.findById(actual.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
        verify(actual, entity.get());
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
        var create = Instancio.of(EmailCreateDto.class)
            .generate(field(EmailCreateDto::getMemberId), g -> g.oneOf(memberIdList))
            .setBlank(nonNullableField)
            .create();

        // perform POST
       sendInvalidCreate(create);
    }

    @Test
    public void create_with_invalid_email_returns_bad_request() throws Exception {
        // setup
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(EmailCreateDto.class)
            .generate(field(EmailCreateDto::getMemberId), g -> g.oneOf(memberIdList))
            .set(field(EmailDto::getEmail), "X")
            .create();

        // perform POST
        sendInvalidCreate(create);
    }

    @Test
    public void update_returns_dto_status_ok() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailUpdateDto.class)
            .set(field(EmailUpdateDto::getId), id)
            .set(field(EmailUpdateDto::getMemberId), memberId)
            .create();

        // perform PUT
        var actual = sendValidUpdate(id, update, EmailDto.class);

        // verify
        var email = repo.findById(actual.getId());

        assertTrue(email.isPresent());
        verify(update, email.get());
        verify(actual, email.get());
    }

    @Test
    public void update_returns_status_bad_request() throws Exception {
        // Setup
        var update = Instancio.create(EmailUpdateDto.class);

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

        var update = Instancio.of(EmailUpdateDto.class)
            .set(field(EmailUpdateDto::getId), id)
            .set(field(EmailUpdateDto::getMemberId), memberId)
            .ignore(nonNullableField)
            .create();

        sendInvalidUpdate(id, update);
    }

    @Test
    public void update_with_invalid_email_returns_bad_request() throws Exception {
        // setup
        var entity = addEntitiesToDb(20).get(10);
        var id = entity.getId();
        var memberId = entity.getMember().getId();

        var update = Instancio.of(EmailUpdateDto.class)
            .set(field(EmailUpdateDto::getId), id)
            .set(field(EmailUpdateDto::getMemberId), memberId)
            .set(field(EmailUpdateDto::getEmail), "XXX")
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

    private void verify(EmailCreateDto expected, EmailEntity actual) {
        verify((EmailBaseDto) expected, actual);
    }

    private void verify(EmailUpdateDto expected, EmailEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        verify((EmailBaseDto) expected, actual);
    }

    private void verify(EmailDto expected, EmailEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        verify((EmailBaseDto) expected, actual);
    }

    private void verify(EmailBaseDto expected, EmailEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(EmailEntity expected, EmailDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(EmailEntity expected, EmailFullNameDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
        assertEquals(expected.getMember().getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getMember().getFirstName(), actual.getFullName().getFirstName());
        assertEquals(expected.getMember().getMiddleName(), actual.getFullName().getMiddleName());
        assertEquals(expected.getMember().getLastName(), actual.getFullName().getLastName());
        assertEquals(expected.getMember().getSuffix(), actual.getFullName().getSuffix());
    }

    private void verify(List<EmailEntity> expected, List<EmailFullNameDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

    private Map<String, SortFields<EmailEntity, EmailFullNameDto>> getSorts() {

        Map<String, SortFields<EmailEntity, EmailFullNameDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(EmailEntity::getId),
            Comparator.comparingInt(EmailFullNameDto::getId)));

        map.put("email", SortFields.of(Comparator.comparing(EmailEntity::getEmail),
            Comparator.comparing(EmailFullNameDto::getEmail)));

        map.put("email_type", SortFields.of(Comparator.comparing(EmailEntity::getEmailType),
            Comparator.comparing(EmailFullNameDto::getEmailType)));

        map.put("member_number", SortFields.of(
            Comparator.comparing(e -> e.getMember().getMemberNumber()),
            Comparator.comparing(EmailFullNameDto::getMemberNumber)));

        map.put("full_name", SortFields.of(
            Comparator.comparing(e -> e.getMember().getLastName()),
            Comparator.comparing(d -> d.getFullName().getLastName())));

        return map;
    }

}
