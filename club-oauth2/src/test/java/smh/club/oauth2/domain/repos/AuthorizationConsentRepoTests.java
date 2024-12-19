package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@Transactional
@ExtendWith(InstancioExtension.class)
// Can't use @DataJpaTest due to some converters need an injected ObjectMapper
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AuthorizationConsentRepoTests {

  @Autowired
  private AuthorizationConsentRepository repo;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.COLLECTION_MAX_SIZE, 2)
          .set(Keys.COLLECTION_MIN_SIZE, 2)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void find_by_client_id_and_principal_name() {
    // setup
    var entities = Instancio.ofList(AuthorizationConsentEntity.class)
        .size(5)
        .create();
    var expected = entities.get(3);

    // execute
    repo.saveAll(entities);
    var optional = repo.findByRegisteredClientIdAndPrincipalName(
        expected.getRegisteredClientId(), expected.getPrincipalName());

    // verify
    assertTrue(optional.isPresent());
    var actual = optional.get();
    assertEquals(expected, actual);
  }

  @Test
  public void delete_by_client_id_and_principal_name() {
    // setup
    var entities = Instancio.ofList(AuthorizationConsentEntity.class)
        .size(5)
        .create();
    var target = entities.get(3);

    // execute
    repo.saveAllAndFlush(entities);
    repo.deleteByRegisteredClientIdAndPrincipalName(
        target.getRegisteredClientId(), target.getPrincipalName());

    var optional = repo.findByRegisteredClientIdAndPrincipalName(
        target.getRegisteredClientId(), target.getPrincipalName());

    // verify
    assertTrue(optional.isEmpty());
  }
}
