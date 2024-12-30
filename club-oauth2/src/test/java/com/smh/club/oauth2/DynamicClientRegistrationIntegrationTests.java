package com.smh.club.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.helpers.TestUtils;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.Header;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@ActiveProfiles({"tests", "prod"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class DynamicClientRegistrationIntegrationTests {

  @LocalServerPort
  int port;

  @Autowired
  private RegisteredClientRepository registeredClientRepository;

  @Autowired
  private UserRepository userRepository;

  private final ObjectMapper mapper;

  @Autowired
  public DynamicClientRegistrationIntegrationTests(MockMvc mockMvc,
                                                   ObjectMapper withoutOauth2) {
    this.mapper =  withoutOauth2;

    // setup RestAssured to use the MockMvc Context
    RestAssuredMockMvc.mockMvc(mockMvc);

    // Configure RestAssured to use the injected Object mapper.
    RestAssuredMockMvc.config =
        RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
            (type, s) -> TestUtils.getObjectMapper()));
  }

  @BeforeEach
  public void setup() {
    addRegistrarClientToDb();
  }

  @Test
  public void whenGetDiscoveryDoc_return_discoveryDoc() {

    var discoveryEndpoint = "http://localhost:" + port + "/.well-known/openid-configuration";

    var result = getDiscoveryInfo(discoveryEndpoint);

    assertNotNull(result);
    assertFalse(result.get("token_endpoint").asText().isBlank());
    assertFalse(result.get("registration_endpoint").asText().isBlank());
    assertFalse(result.get("authorization_endpoint").asText().isBlank());
  }

  @Test
  public void registerClient_dynamically() throws Exception {
    // discover the endpoints
    var discoveryEndpoint = "http://localhost:" + port + "/.well-known/openid-configuration";
    var discoveryInfo = getDiscoveryInfo(discoveryEndpoint);

    // resolve endpoints from the returned discovery doc
    var tokenEndpoint = discoveryInfo.get("token_endpoint").asText();
    var registrationEndpoint = discoveryInfo.get("registration_endpoint").asText();

    // get registration token
    var tokenResponse = getRegistrationToken(tokenEndpoint);
    var token = tokenResponse.get("access_token").asText();
    assertFalse(token.isBlank());

    // register client
    var request = Map.of(
        "client_name", "test-client",
        "grant_types", List.of("authorization_code", "client_credentials"),
        "scope", String.join(" ", "openid email profile"),
        "redirect_uris", List.of("http://localhost:" + port + "/oauth2/callback"));

    var registerResponse = registerClient(token, request, registrationEndpoint);
    assertTrue(registerResponse.isObject());
    assertEquals("test-client", registerResponse.get("client_name").asText());
    assertFalse(registerResponse.get("client_id").asText().isBlank());
    assertFalse(registerResponse.get("client_secret").asText().isBlank());
    assertNull(registerResponse.get("client_uri"));
    assertTrue(registerResponse.get("scope").asText().contains("openid"));
    assertTrue(registerResponse.get("scope").asText().contains("email"));
    assertTrue(registerResponse.get("scope").asText().contains("profile"));
    assertTrue(registerResponse.get("grant_types").toString().contains("authorization_code"));
    assertTrue(registerResponse.get("grant_types").toString().contains("client_credentials"));
    assertTrue(registerResponse.get("redirect_uris").toString()
        .contains("http://localhost:" + port + "/oauth2/callback"));
    assertFalse(registerResponse.get("registration_access_token").asText().isBlank());
    assertFalse(registerResponse.get("registration_client_uri").asText().isBlank());

    // retrieve registered client
    var registrationAccessToken = registerResponse.get("registration_access_token").asText();
    var clientUri = registerResponse.get("registration_client_uri").asText();

    var retrieveResponse = retrieveClient(registrationAccessToken, clientUri);
    assertEquals("test-client", retrieveResponse.get("client_name").asText());
    assertFalse(retrieveResponse.get("client_id").asText().isBlank());
    assertFalse(retrieveResponse.get("client_secret").asText().isBlank());
    assertTrue(retrieveResponse.get("scope").asText().contains("openid"));
    assertTrue(retrieveResponse.get("scope").asText().contains("email"));
    assertTrue(retrieveResponse.get("scope").asText().contains("profile"));
    assertTrue(retrieveResponse.get("grant_types").toString().contains("authorization_code"));
    assertTrue(retrieveResponse.get("grant_types").toString().contains("client_credentials"));
    assertTrue(retrieveResponse.get("redirect_uris").toString()
        .contains("http://localhost:" + port + "/oauth2/callback"));
    assertFalse(registerResponse.get("registration_access_token").asText().isBlank());
    assertFalse(registerResponse.get("registration_client_uri").asText().isBlank());

  }

  private ObjectNode getDiscoveryInfo(String discoveryEndpoint) {
    return
        given()
            .when()
              .get(discoveryEndpoint)
            .then()
              .statusCode(HttpStatus.OK.value()).assertThat()
              .extract().body().as(ObjectNode.class);
  }

  private ObjectNode getRegistrationToken(String endpoint) {

   return
        given()
          .auth().with(httpBasic("registrar-client", "secret"))
          .param("grant_type", "client_credentials")
          .param("scope", "client.create")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .when()
            .post(endpoint)
        .then()
          .extract().body().as(ObjectNode.class);

  }

  public ObjectNode registerClient(String tokenValue, Map<String, Object> request, String registrationEndpoint) throws Exception {

    var header = new Header("Authorization", "Bearer " + tokenValue);
    var body = mapper.writeValueAsString(request);

    return
        given()
          .contentType(MediaType.APPLICATION_JSON)
          .header(header)
          .body(body)
        .when()
          .post(registrationEndpoint)
        .then()
          .status(HttpStatus.CREATED)
          .extract().body().as(ObjectNode.class);
  }

  public ObjectNode retrieveClient(String registrationAccessToken, String clientUri) {

    var header = new Header("Authorization", "Bearer " + registrationAccessToken);

    return
        given()
            .header(header)
        .when()
            .get(clientUri)
        .then()
            .status(HttpStatus.OK)
            .extract().body().as(ObjectNode.class);
  }

  private void addRegistrarClientToDb() {
    var registrarClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("registrar-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("client.create")
        .scope("client.read")
        .build();

    registeredClientRepository.save(registrarClient);

      var user = UserDetailsEntity.builder()
          .username("user1")
          .password("{noop}password")
          .firstName(Instancio.create(String.class))
          .lastName(Instancio.create(String.class))
          .email(Instancio.create(String.class))
          .enabled(true)
          .build();

      var authority = GrantedAuthorityEntity.builder()
          .authority("USER")
          .build();
      user.addGrantedAuthority(authority);
      userRepository.save(user);

  }
}
