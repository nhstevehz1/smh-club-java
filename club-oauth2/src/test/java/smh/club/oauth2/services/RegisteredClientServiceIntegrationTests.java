package smh.club.oauth2.services;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.repos.ClientRepository;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles({"tests", "prod"})
@Transactional
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class RegisteredClientServiceIntegrationTests {

  @Autowired
  private RegisteredClientRepository service;

  @Autowired
  private ClientRepository repo;

  @WithSettings
      Settings settings =
        Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true);


  @Test
  public void save_registered_client() {
    // setup
    var expected = createRegisteredClient();

    // execute
    service.save(expected);

    // verify
    var actual = repo.findById(expected.getId()).orElseThrow();

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getClientId(), actual.getClientId());
    assertEquals(expected.getClientSecret(), actual.getClientSecret());
    assertEquals(expected.getClientIdIssuedAt(), actual.getClientIdIssuedAt());
    assertEquals(expected.getClientAuthenticationMethods(), actual.getClientAuthenticationMethods());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());
    assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
    assertEquals(expected.getScopes(), actual.getScopes());
    assertEquals(expected.getPostLogoutRedirectUris(), actual.getPostLogoutRedirectUris());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());

  }

  @Test
  public void find_registered_client_by_id() {
    // setup
    var expected = createRegisteredClient();
    service.save(expected);

    // execute
    service.findById(expected.getId());

    // verify
    var actual = service.findById(expected.getId());

    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getClientId(), actual.getClientId());
    assertEquals(expected.getClientSecret(), actual.getClientSecret());
    assertEquals(expected.getClientIdIssuedAt(), actual.getClientIdIssuedAt());
    assertEquals(expected.getClientAuthenticationMethods(), actual.getClientAuthenticationMethods());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());
    assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
    assertEquals(expected.getScopes(), actual.getScopes());
    assertEquals(expected.getPostLogoutRedirectUris(), actual.getPostLogoutRedirectUris());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());
  }

  @Test
  public void find_registered_client_by_client_id() {
    // setup
    var expected = createRegisteredClient();

    // execute
    service.save(expected);
    var actual = service.findByClientId(expected.getClientId());

    // verify
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getClientId(), actual.getClientId());
    assertEquals(expected.getClientSecret(), actual.getClientSecret());
    assertEquals(expected.getClientIdIssuedAt(), actual.getClientIdIssuedAt());
    assertEquals(expected.getClientAuthenticationMethods(), actual.getClientAuthenticationMethods());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());
    assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
    assertEquals(expected.getScopes(), actual.getScopes());
    assertEquals(expected.getPostLogoutRedirectUris(), actual.getPostLogoutRedirectUris());
    assertEquals(expected.getAuthorizationGrantTypes(), actual.getAuthorizationGrantTypes());
  }

  private RegisteredClient createRegisteredClient() {
    return  RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("messaging-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
        .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .scope("message.read")
        .scope("message.write")
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();
  }

}
