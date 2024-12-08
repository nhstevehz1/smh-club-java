package smh.club.oauth2.mappers;

import java.time.Instant;
import java.util.UUID;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import smh.club.oauth2.domain.entities.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(InstancioExtension.class)
public class RegisteredClientTests {

  private final RegisteredClientMapperImpl clientMapper =
      new RegisteredClientMapperImpl();

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true);

  @Test
  public void from_registeredClient_to_client() {
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
    var client = clientMapper.toClientEntity(rc);

    // verify
    assertNotNull(client);
    assertEquals(client.getId(), UUID.fromString(rc.getId()));
    assertEquals(rc.getClientId(), client.getClientId());
    assertEquals(rc.getClientName(), client.getClientName());
    assertEquals(rc.getClientSecret(), client.getClientSecret());
    assertEquals(rc.getClientIdIssuedAt(), client.getClientIdIssuedAt());
    assertEquals(rc.getClientSecretExpiresAt(), client.getClientSecretExpiresAt());
    assertEquals(rc.getClientAuthenticationMethods(), client.getClientAuthenticationMethods());
    assertEquals(rc.getAuthorizationGrantTypes(), client.getAuthorizationGrantTypes());
    assertEquals(rc.getRedirectUris(), client.getRedirectUris());
    assertEquals(rc.getPostLogoutRedirectUris(), client.getPostLogoutRedirectUris());
    assertEquals(rc.getScopes(), client.getScopes());
    assertEquals(rc.getClientSettings(), client.getClientSettings());
    assertEquals(rc.getTokenSettings(), client.getTokenSettings());
  }

  @Test
  public void from_client_to_registeredClient() {
    // setup
    var client = Client.builder()
        .id(UUID.randomUUID())
        .clientId("messaging-client")
        .clientName("messaging-client")
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
    var rc = clientMapper.toRegisteredClient(client);

    // verify
    assertEquals( UUID.fromString(rc.getId()), client.getId());
    assertEquals(client.getClientId(), rc.getClientId());
    assertEquals(client.getClientName(), rc.getClientName());
    assertEquals(client.getClientSecret(), rc.getClientSecret());
    assertEquals(client.getClientIdIssuedAt(), rc.getClientIdIssuedAt());
    assertEquals(client.getClientSecretExpiresAt(), rc.getClientSecretExpiresAt());
    assertEquals(client.getClientAuthenticationMethods(), rc.getClientAuthenticationMethods());
    assertEquals(client.getAuthorizationGrantTypes(), rc.getAuthorizationGrantTypes());
    assertEquals(client.getRedirectUris(), rc.getRedirectUris());
    assertEquals(client.getPostLogoutRedirectUris(), rc.getPostLogoutRedirectUris());
    assertEquals(client.getScopes(), rc.getScopes());
    assertEquals(client.getClientSettings(), rc.getClientSettings());
    assertEquals(client.getTokenSettings(), rc.getTokenSettings());
  }
}
