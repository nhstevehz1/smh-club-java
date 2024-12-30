package com.smh.club.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.GrantedAuthorityRepo;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.RoleDto;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Comparator;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class RolesControllerIntegrationTests {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private GrantedAuthorityRepo gaRepo;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mvc;

  private final String path = "/api/v1/users";

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

  @Test
  public void add_role_returns_roleDto_status_created() throws Exception {
    // setup
    var entity =addEntitiesToDb(5).get(2);
    var id = entity.getId();
    var role = "TEST";
    var dto = RoleDto.builder().roleName(role).build();

    // perform post
    var ret =
        given()
            .pathParam("id", id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(dto))
        .when()
          .post(path + "/{id}/roles")
        .then()
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().as(RoleDto.class);

    // verify
    var actual = gaRepo.findById(ret.getId());

    assertNotNull(actual);
    assertTrue(actual.isPresent());
    assertEquals(role, ret.getRoleName());
    assertEquals(role, actual.get().getAuthority());
  }

  @Test
  public void add_role_user_not_found_returns_status_bad_request() throws Exception {
    // setup
    var highest = addEntitiesToDb(5).stream()
        .max(Comparator.comparingLong(UserDetailsEntity::getId))
        .map(UserDetailsEntity::getId).orElseThrow();

    long id = highest + 100;
    var role = "TEST";
    var dto = RoleDto.builder().roleName(role).build();

    // perform post and verify
    given()
        .pathParam("id", id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(dto))
      .when()
        .post(path + "/{id}/roles")
      .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  public void delete_returns_void_status_no_content() throws Exception {
    // setup
    var entity =addEntitiesToDb(10).get(5);
    var userId = entity.getId();
    var ga = entity.getAuthorities().iterator().next();
    var role = RoleDto.builder().id(ga.getId()).roleName(ga.getAuthority()).build();

    // execute
    given()
        .pathParam("userId", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(role))
      .when()
        .delete(path + "/{userId}/roles")
      .then()
        .status(HttpStatus.NO_CONTENT);

    // verify
    var actual = gaRepo.findById(role.getId());
    assertTrue(actual.isEmpty());
  }

  private List<UserDetailsEntity> addEntitiesToDb(int size) {
    var entities = Instancio.ofList(UserDetailsEntity.class)
        .size(size)
        .ignore(field(UserDetailsEntity::getId))
        .create();

    entities.forEach(e ->{
      e.addGrantedAuthority(GrantedAuthorityEntity.builder().authority("ADMIN").build());
      e.addGrantedAuthority(GrantedAuthorityEntity.builder().authority("USER").build());
    });

    return userRepo.saveAllAndFlush(entities);
  }
}
