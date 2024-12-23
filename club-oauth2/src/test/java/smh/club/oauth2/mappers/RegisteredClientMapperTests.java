package smh.club.oauth2.mappers;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import smh.club.oauth2.domain.entities.ClientEntity;
import smh.club.oauth2.helpers.OAuth2MapperTestBase;
import smh.club.oauth2.helpers.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class RegisteredClientMapperTests extends OAuth2MapperTestBase {

  private RegisteredClientMapperImpl clientMapper;

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  void setup() {
    clientMapper = new RegisteredClientMapperImpl(TestUtils.getObjectMapper());
  }

  @Test
  public void from_registeredClient_to_client_entity()  {
    // setup
    RegisteredClient rc = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("messaging-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientSecretExpiresAt(Instant.now().plusSeconds(60))
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
        .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
        .build();

    // execute
    var entity = clientMapper.toClientEntity(rc);

    // verify
    assertNotNull(entity);
    assertEquals(entity.getId(), rc.getId());
    assertEquals(rc.getClientId(), entity.getClientId());
    assertEquals(rc.getClientName(), entity.getClientName());
    assertEquals(rc.getClientSecret(), entity.getClientSecret());
    assertEquals(rc.getClientIdIssuedAt(), entity.getClientIdIssuedAt());
    assertEquals(rc.getClientSecretExpiresAt(), entity.getClientSecretExpiresAt());

    assertTrue(rc.getClientAuthenticationMethods().containsAll(entity.getClientAuthenticationMethods()));
    assertTrue(rc.getAuthorizationGrantTypes().containsAll(entity.getAuthorizationGrantTypes()));

    assertTrue(rc.getRedirectUris().containsAll(entity.getRedirectUris()));
    assertTrue(rc.getPostLogoutRedirectUris().containsAll(entity.getPostLogoutRedirectUris()));
    assertTrue(rc.getScopes().containsAll(entity.getScopes()));

    assertEquals(rc.getClientSettings().getSettings().entrySet(), parseMap(entity.getClientSettings()).entrySet());
    assertEquals(rc.getTokenSettings().getSettings().entrySet(), parseMap(entity.getTokenSettings()).entrySet());
  }

  @Test
  public void from_clientEntity_to_registeredClient() {
    // setup
    var tokenSettings = Instancio.ofMap(String.class, Object.class).size(5).create();
    var clientSettings = Instancio.ofMap(String.class, Object.class).size(5).create();

    var entity = ClientEntity.builder()
        .id(String.valueOf(UUID.randomUUID()))
        .clientId("messaging-client")
        .clientName("messaging-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientSecretExpiresAt(Instant.now().plusSeconds(60))
        .clientAuthenticationMethods(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC))
        .authorizationGrantTypes(Set.of(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN))
        .redirectUris(Set.of("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc"))
        .postLogoutRedirectUris(Set.of("http://127.0.0.1:8080/logged-out"))
        .scopes(Set.of(OidcScopes.OPENID, OidcScopes.PROFILE))
        .clientSettings(writeMap(clientSettings))
        .tokenSettings(writeMap(tokenSettings))
        .build();

    // execute
    var rc = clientMapper.toRegisteredClient(entity);

    // verify
    assertEquals(entity.getId(), rc.getId());
    assertEquals(entity.getClientId(), rc.getClientId());
    assertEquals(entity.getClientName(), rc.getClientName());
    assertEquals(entity.getClientSecret(), rc.getClientSecret());
    assertEquals(entity.getClientIdIssuedAt(), rc.getClientIdIssuedAt());
    assertEquals(entity.getClientSecretExpiresAt(), rc.getClientSecretExpiresAt());
    assertEquals(entity.getClientAuthenticationMethods(), rc.getClientAuthenticationMethods());
    assertEquals(entity.getAuthorizationGrantTypes(), rc.getAuthorizationGrantTypes());
    assertEquals(entity.getRedirectUris(), rc.getRedirectUris());
    assertEquals(entity.getPostLogoutRedirectUris(), rc.getPostLogoutRedirectUris());
    assertEquals(entity.getScopes(), rc.getScopes());
    assertEquals(parseMap(entity.getClientSettings()).entrySet(), rc.getClientSettings().getSettings().entrySet());
    assertEquals(parseMap(entity.getTokenSettings()).entrySet(), rc.getTokenSettings().getSettings().entrySet());
  }
}
