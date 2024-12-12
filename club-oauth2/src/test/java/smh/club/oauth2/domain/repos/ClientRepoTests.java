package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Set;
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

    var client = Instancio.of(ClientEntity.class)
        .set(field(ClientEntity::getAuthorizationGrantTypes), grantTypes)
        .set(field(ClientEntity::getClientAuthenticationMethods), authMethods)
        .create();

    // execute
    var saved = clientRepository.saveAndFlush(client);
    var actual = clientRepository.findByClientId(client.getClientId());

    // assert
    assertTrue(actual.isPresent());
    assertEquals(client.getClientId(), saved.getClientId());
    assertEquals(client.getClientId(), actual.get().getClientId());
    assertEquals(client.getClientSecret(), actual.get().getClientSecret());
  }
}
