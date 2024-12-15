package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
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
import smh.club.oauth2.domain.entities.AuthorizationEntity;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AuthorizationRepoTests {

  @Autowired
  private AuthorizationRepository repo;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.COLLECTION_MAX_SIZE, 2)
          .set(Keys.COLLECTION_MIN_SIZE, 2)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void find_by_state() {
    // setup
    var list = Instancio.ofList(AuthorizationEntity.class)
        .size(5)
        .generate(field(AuthorizationEntity::getId), g -> g.string().upperCase().length(10))
        .create();

    var expected = list.get(3);

    // execute
    repo.saveAll(list);
    var entity =  repo.findByState(expected.getState());

    assertTrue(entity.isPresent());
    var actual = entity.get();
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getRegisteredClientId(), actual.getRegisteredClientId());
    assertEquals(expected.getAuthorizationGrantType(), actual.getAuthorizationGrantType());
    assertEquals(expected.getPrincipalName(), actual.getPrincipalName());
    assertEquals(expected.getAuthorizedScopes(), actual.getAuthorizedScopes());
    assertEquals(expected.getAttributes(), actual.getAttributes());
    assertEquals(expected.getTokens(), actual.getTokens());
  }

  @Test
  public void findById_authId_tokenType_not_unique_throws_exception () {
    // setup
    var auth = Instancio.create(AuthorizationEntity.class);
    var list = auth.getTokens().stream().toList();
    list.get(1).setTokenType(list.get(0).getTokenType());

    // execute and verify
    assertThrows(Exception.class, () -> repo.save(auth));

  }

}
