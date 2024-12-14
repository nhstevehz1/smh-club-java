package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Set;
import java.util.UUID;
import org.instancio.Instancio;
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
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.ClientEntity;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class ClientRepoTests {

  @Autowired
  private ClientRepository clientRepository;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void find_by_client_id() {
    // setup
    var grantTypes = Set.of(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.CLIENT_CREDENTIALS);
    var authMethods = Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, ClientAuthenticationMethod.CLIENT_SECRET_POST);

    var expected = Instancio.of(ClientEntity.class)
        .set(field(ClientEntity::getId), UUID.randomUUID().toString())
        .set(field(ClientEntity::getAuthorizationGrantTypes), grantTypes)
        .set(field(ClientEntity::getClientAuthenticationMethods), authMethods)
        .create();

    // execute
    clientRepository.saveAndFlush(expected);
    var optional = clientRepository.findByClientId(expected.getClientId());

    // assert
    assertTrue(optional.isPresent());
    var actual = optional.get();
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getClientId(), actual.getClientId());

    assertEquals(expected.getClientIdIssuedAt().getEpochSecond(),
        actual.getClientIdIssuedAt().getEpochSecond());

    assertEquals(expected.getClientSecret(), actual.getClientSecret());

    assertEquals(expected.getClientSecretExpiresAt().getEpochSecond(),
        actual.getClientSecretExpiresAt().getEpochSecond());

    assertEquals(actual.getClientSettings(), expected.getClientSettings());
    assertEquals(actual.getTokenSettings(), expected.getTokenSettings());
    assertEquals(actual.getClientAuthenticationMethods(), expected.getClientAuthenticationMethods());
    assertEquals(actual.getAuthorizationGrantTypes(), expected.getAuthorizationGrantTypes());
    assertEquals(actual.getClientAuthenticationMethods(), expected.getClientAuthenticationMethods());
    assertEquals(actual.getRedirectUris(), expected.getRedirectUris());
    assertEquals(actual.getPostLogoutRedirectUris(), expected.getPostLogoutRedirectUris());
    assertEquals(actual.getScopes(), expected.getScopes());
  }
}
