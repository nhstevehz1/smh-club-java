package com.smh.club.oauth2;

import static java.util.Comparator.comparingLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import com.smh.club.oauth2.dto.UserDto;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.List;
import java.util.stream.Stream;
import org.instancio.Instancio;
import org.instancio.Selector;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WithMockUser
@ActiveProfiles({"tests"})
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class UserControllerIntegrationTests {

  @Autowired
  private UserRepository repo;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mvc;

  private final String path = "/api/v1/users";

  @WithSettings // Instancio settings
  private final Settings settings =
      Settings.create().set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE,0)
          .set(Keys.BEAN_VALIDATION_ENABLED, true);

  @BeforeEach
  public void setup() {
    // setup RestAssured to use the MockMvc Context
    RestAssuredMockMvc.mockMvc(mvc);

    // add mock csrf token
    RestAssuredMockMvc.postProcessors(csrf().asHeader());

    // Configure RestAssured to use the injected Object mapper.
    RestAssuredMockMvc.config =
        RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
            (type, s) -> mapper));
  }

  @AfterEach
  public void tearDown() {
    RestAssuredMockMvc.reset();
  }
  
  @RepeatedTest(5)
  public void get_returns_dto_status_ok() {
    // setup
    var expected = addEntitiesToDb(20).get(10);
    var id = expected.getId();

    // perform get
    var actual =
        given()
            .pathParam("id", id)
            .accept(MediaType.APPLICATION_JSON)
        .when()
            .get(path + "/{id}")
        .then()
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(UserDto.class);

    // verify
    verify(expected, actual);
  }

  @Test
  public void get_returns_status_not_found() {
    // Setup
    var entities = addEntitiesToDb(5);
    var highest = entities.stream()
        .max(comparingLong(UserDetailsEntity::getId))
        .map(UserDetailsEntity::getId).orElseThrow();
    var id = highest + 100;

    // perform get and verify
    given()
        .pathParam("id", id)
        .accept(MediaType.APPLICATION_JSON)
      .when()
        .get( path + "/{id}")
      .then()
        .status(HttpStatus.NOT_FOUND);

  }

  @RepeatedTest(5)
  public void get_details_returns_dto_status_ok() {
    // setup
    var expected = addEntitiesToDb(20).get(10);
    var id = expected.getId();

    // perform get
    var actual =
        given()
            .pathParam("id", id)
            .accept(MediaType.APPLICATION_JSON)
          .when()
            .get(path + "/{id}/details")
          .then()
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(UserDetailsDto.class);

    // verify
    verify(expected, actual);
  }

  @Test
  public void getDetails_returns_not_found() {
    // Setup
    var entities = addEntitiesToDb(5);
    var highest = entities.stream()
        .max(comparingLong(UserDetailsEntity::getId))
        .map(UserDetailsEntity::getId).orElseThrow();
    var id = highest + 100;

    // perform get and verify
    given()
        .pathParam("id", id)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get( path + "/{id}/details")
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  public void create_returns_dto_status_created() throws Exception {
    // setup
    var create = Instancio.create(CreateUserDto.class);

    // perform POST
    var ret =
      given()
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .body(mapper.writeValueAsString(create))
        .when()
          .post(path)
        .then()
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .extract().body().as(UserDetailsDto.class);

    // verify
    var entity = repo.findById(ret.getId());

    assertTrue(entity.isPresent());
    verify(create, entity.get());
  }

  // used with test that follows
  private static Stream<Arguments> createNonNullableFields() {
    return Stream.of(
        arguments(field(CreateUserDto::getUsername)),
        arguments(field(CreateUserDto::getFirstName)),
        arguments(field(CreateUserDto::getLastName)));
  }

  @ParameterizedTest
  @MethodSource("createNonNullableFields")
  public void create_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
    // setup
    var create = Instancio.of(CreateUserDto.class)
        .setBlank(nonNullableField)
        .create();

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(create))
      .when()
        .post(path)
      .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  private static Stream<Arguments> createNullableFields() {
    return Stream.of(
        arguments(field(CreateUserDto::getMiddleName)));
  }

  @ParameterizedTest
  @MethodSource("createNullableFields")
  public void create_nullableField_returns_dto_status_created(Selector nullableField) throws Exception {
    // setup
    var create = Instancio.of(CreateUserDto.class)
        .setBlank(nullableField)
        .create();

    // perform POST
    var ret =
        given()
            .postProcessors(csrf().asHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(create))
            .when()
            .post(path)
            .then()
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(UserDetailsDto.class);

    // verify
    var entity = repo.findById(ret.getId());

    assertTrue(entity.isPresent());
    verify(create, entity.get());
  }

  @Test
  public void update_returns_dto_with_status_ok() throws Exception {
    // setup
    var entity = addEntitiesToDb(5).get(2);
    var id = entity.getId();
    var update = Instancio.of(UserDetailsDto.class)
        .set(field(UserDetailsDto::getId), id)
        .create();

    // perform PUT
    var actual =
        given()
            .pathParam("id", id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
        .when()
          .put(path + "/{id}")
        .then()
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(UserDetailsDto.class);

    var user = repo.findById(id);

    assertTrue(user.isPresent());
    assertEquals(update, actual);
    verify(update, user.get());
  }

  @Test
  public void update_status_not_found() throws Exception {
    // setup
    var update = Instancio.create(UserDetailsDto.class);
    var id = update.getId();

    // perform put and verify
    given()
        .pathParam("id", id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(update))
      .when()
        .put(path + "/{id}")
      .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void reset_password_returns_ok()  {
    // setup
    var entity = addEntitiesToDb(5).get(2);
    var id = entity.getId();

    // execute
    given()
        .pathParam("id", id)
      .when()
        .put(path + "/{id}/pwd")
      .then()
        .status(HttpStatus.NO_CONTENT);

    // verify
    var actual = repo.findById(id);

    assertTrue(actual.isPresent());
    assertNotEquals(entity.getPassword(), actual.get().getPassword());

  }

  @Test
  public void reset_password_user_not_found_returns_notFound()  {
    // setup
    var entities = addEntitiesToDb(5);
    var highest = entities.stream()
        .max(comparingLong(UserDetailsEntity::getId))
        .map(UserDetailsEntity::getId).orElseThrow();
    var id = highest + 100;

    // execute
    given()
        .pathParam("id", id)
        .when()
        .put(path + "/{id}/pwd")
        .then()
        .status(HttpStatus.NOT_FOUND);

  }

  // used with test that follows
  private static Stream<Arguments> updateNonNullableFields() {
    return Stream.of(
        arguments(field(UserDetailsDto::getUsername)),
        arguments(field(UserDetailsDto::getFirstName)),
        arguments(field(UserDetailsDto::getLastName)));
  }

  @ParameterizedTest
  @MethodSource("updateNonNullableFields")
  public void update_with_nonNullable_field_returns_bad_request(Selector nonNullableField) throws Exception {
    // setup
    var entity = addEntitiesToDb(5).get(2);
    var id = entity.getId();
    var update = Instancio.of(UserDetailsDto.class)
        .set(field(UserDetailsDto::getId), id)
        .setBlank(nonNullableField)
        .create();

    // Perform POST
    given()
        .pathParam("id", id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(update))
      .when()
        .put(path + "/{id}")
      .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  // used with test that follows
  private static Stream<Arguments> updateNullableFields() {
    return Stream.of(
        arguments(field(UserDetailsDto::getMiddleName)));
  }

  @ParameterizedTest
  @MethodSource("updateNullableFields")
  public void update_with_nullable_files_returns_dto_status_ok(Selector nullableField) throws Exception {
    // setup
    var entity = addEntitiesToDb(5).get(2);
    var id = entity.getId();
    var update = Instancio.of(UserDetailsDto.class)
        .set(field(UserDetailsDto::getId), id)
        .setBlank(nullableField)
        .create();

    // perform put
    var actual =
        given()
            .pathParam("id", id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(update))
        .when()
            .put(path + "/{id}")
        .then()
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(UserDetailsDto.class);

    // verify
    var user = repo.findById(id);
    assertTrue(user.isPresent());
    assertEquals(update, actual);
    verify(user.get(), actual);
  }

  @Test
  public void delete_returns_no_content() {
    // setup
    var entity = addEntitiesToDb(5).get(2);
    var id = entity.getId();

    // perform delete
    given()
        .log().all()
        .pathParam("id", id)
      .when()
        .delete(path + "/{id}")
      .then()
        .status(HttpStatus.NO_CONTENT);

    // verify
    var user = repo.findById(id);
    assertTrue(user.isEmpty());
  }

  private List<UserDetailsEntity> addEntitiesToDb(int size) {
    var entities = Instancio.ofList(UserDetailsEntity.class)
        .size(size)
        .ignore(field(UserDetailsEntity::getId))
        .withUnique(field(UserDetailsEntity::getUsername))
        .create();

    return repo.saveAllAndFlush(entities);
  }

  private void verify(UserDetailsEntity expected, UserDto actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
  }

  private void verify(UserDetailsEntity expected, UserDetailsDto actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
    assertEquals(expected.isAccountNonExpired(), actual.isAccountNonExpired());
    assertEquals(expected.isAccountNonLocked(), actual.isAccountNonLocked());
    assertEquals(expected.isCredentialsNonExpired(), actual.isCredentialsNonExpired());
    assertEquals(expected.isEnabled(), actual.isEnabled());
  }

  private void verify(CreateUserDto expected, UserDetailsEntity actual) {
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
    assertEquals(expected.isAccountNonExpired(), actual.isAccountNonExpired());
    assertEquals(expected.isAccountNonLocked(), actual.isAccountNonLocked());
    assertEquals(expected.isCredentialsNonExpired(), actual.isCredentialsNonExpired());
    assertEquals(expected.isEnabled(), actual.isEnabled());
  }

  private void verify(UserDetailsDto expected, UserDetailsEntity actual) {
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
    assertEquals(expected.isAccountNonExpired(), actual.isAccountNonExpired());
    assertEquals(expected.isAccountNonLocked(), actual.isAccountNonLocked());
    assertEquals(expected.isCredentialsNonExpired(), actual.isCredentialsNonExpired());
    assertEquals(expected.isEnabled(), actual.isEnabled());
  }
}
