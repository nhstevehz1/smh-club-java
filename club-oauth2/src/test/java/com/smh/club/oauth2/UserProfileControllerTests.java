package com.smh.club.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.dto.ProfileDto;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ActiveProfiles({"tests"})
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class UserProfileControllerTests {

  private static final String USERNAME = "user1";
  private static final String UNKNOWN = "123-unknown-user";
  private static final String PASSWORD = "password";

  @Autowired
  private UserRepository repo;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private PasswordEncoder encoder;

  private final String path = "/api/v1/profiles";

  @WithSettings // Instancio settings
  private final Settings settings =
      Settings.create()
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

  @WithMockUser(username = USERNAME)
  @Test
  public void get_user_profile_returns_profile_status_ok() {
    // setup
    var user = addEntitiesToDb();

    // execute
    var ret =
      given()
          .accept(MediaType.APPLICATION_JSON)
        .when()
          .get(path)
        .then()
          .status(HttpStatus.OK)
          .extract().body().as(ProfileDto.class);

    // verify
    var actual = repo.findByUsername(user.getUsername());

    assertTrue(actual.isPresent());
    verify(user, ret);
    verify(ret, actual.get());
  }

  @WithMockUser(username = UNKNOWN)
  @Test
  public void get_user_profile_returns_status_not_found() {
    // execute and verify
        given()
            .accept(MediaType.APPLICATION_JSON)
          .when()
            .get(path)
          .then()
            .status(HttpStatus.NOT_FOUND);
  }

  @WithMockUser(username = USERNAME)
  @Test
  public void update_profile_returns_profile_status_ok() throws Exception {
    // setup
    addEntitiesToDb();
    var update = Instancio.of(ProfileDto.class)
        .set(field(ProfileDto::getUsername), "0123456789")
        .create();

    // execute
    var ret =
      given()
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .body(mapper.writeValueAsString(update))
        .when()
          .put(path)
        .then()
          .status(HttpStatus.OK)
          .extract().body().as(ProfileDto.class);

    // verify
    var actual = repo.findByUsername(update.getUsername());

    assertTrue(actual.isPresent());
    assertEquals(update, ret);
    verify(ret, actual.get());
  }

  @WithMockUser(username = UNKNOWN)
  @Test
  public void update_profile_returns_profile_status_not_found() throws Exception {
    addEntitiesToDb();
    var update = Instancio.of(ProfileDto.class)
        .set(field(ProfileDto::getUsername), "0123456789")
        .create();

    // execute and verify
    given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(update))
      .when()
        .put(path)
      .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @WithMockUser(username = USERNAME)
  @Test
  public void change_password_returns_status_ok() throws Exception {
    // setup
    var entity = addEntitiesToDb();
    var newPassword = "newPassword";

    var dto = ChangePasswordDto.builder()
        .oldPassword(PASSWORD)
        .newPassword(newPassword)
        .build();

    // execute
    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(dto))
      .when()
        .put(path + "/pwd")
      .then()
        .status(HttpStatus.OK);

    // verify
    var actual = repo.findByUsername(entity.getUsername());
    assertTrue(actual.isPresent());
    assertTrue(encoder.matches(newPassword, actual.get().getPassword()));
  }

  @WithMockUser(username = USERNAME)
  @Test
  public void change_password_returns_bad_request() throws Exception {
    // setup
    var entity = addEntitiesToDb();
    var oldPassword = "notAMatch";
    var newPassword = "newPassword";

    var dto = ChangePasswordDto.builder()
        .oldPassword(oldPassword)
        .newPassword(newPassword)
        .build();

    // execute
    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(dto))
        .when()
        .put(path + "/pwd")
        .then()
        .status(HttpStatus.BAD_REQUEST);

    // verify
    var actual = repo.findByUsername(entity.getUsername());
    assertTrue(actual.isPresent());
    assertFalse(encoder.matches(newPassword, actual.get().getPassword()));
    assertTrue(encoder.matches(PASSWORD, actual.get().getPassword()));
  }

  @WithMockUser(username = UNKNOWN)
  @Test
  public void change_password_returns_status_not_found() throws Exception {
    // setup
    var entity = addEntitiesToDb();
    var newPassword = "newPassword";

    var dto = ChangePasswordDto.builder()
        .oldPassword(PASSWORD)
        .newPassword(newPassword)
        .build();

    // execute
    given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(dto))
        .when()
        .put(path + "/pwd")
        .then()
        .status(HttpStatus.NOT_FOUND);

    // verify
    var actual = repo.findByUsername(entity.getUsername());
    assertTrue(actual.isPresent());
    assertFalse(encoder.matches(newPassword, actual.get().getPassword()));
    assertTrue(encoder.matches(PASSWORD, actual.get().getPassword()));
  }

  private UserDetailsEntity addEntitiesToDb() {
    var entities = Instancio.ofList(UserDetailsEntity.class)
        .size(5)
        .ignore(field(UserDetailsEntity::getId))
        .create();

    var entity = entities.get(2);
    entity.setUsername(USERNAME);
    entity.setPassword(encoder.encode(PASSWORD));

    repo.saveAllAndFlush(entities);

    return entities.get(2);
  }

  private void verify(UserDetailsEntity expected, ProfileDto actual) {
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getFirstName(), actual.getFirstName());
  }

  private void verify(ProfileDto expected, UserDetailsEntity actual) {
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getFirstName(), actual.getFirstName());
  }
}
