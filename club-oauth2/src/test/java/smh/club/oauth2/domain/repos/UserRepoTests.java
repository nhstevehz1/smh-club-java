package smh.club.oauth2.domain.repos;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.UserEntity;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("tests")
@Transactional
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class UserRepoTests {

  @Autowired
  private UserRepository repo;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setUp() {
    var entities = Instancio.ofList(UserEntity.class)
        .size(5)
        .withUnique(field(UserEntity::getUsername))
        .create();

    repo.saveAllAndFlush(entities);
  }

  @Test
  public void find_by_username() {
    // setup
    var entity = repo.findAll().get(3);

    // execute
    var optional = repo.findByUsername(entity.getUsername());

    // verify
    assertTrue(optional.isPresent());
    assertEquals(entity, optional.get());

  }

  @Test
  public void find_by_password() {
    // setup
    var entity = repo.findAll().get(3);

    // execute
    var optional = repo.findByPassword(entity.getPassword());

    // verify
    assertTrue(optional.isPresent());
    assertEquals(entity, optional.get());
  }

  @Test
  public void exist_by_username() {
    // setup
    var entity = repo.findAll().get(3);

    // execute
    var exists = repo.existsByUsername(entity.getUsername());

    // verify
    assertTrue(exists);
  }

  @Test
  public void delete_by_username() {
    // setup
    var entity = repo.findAll().get(3);

    // execute
    repo.deleteByUsername(entity.getUsername());
    var optional = repo.findByPassword(entity.getUsername());

    // verify
    assertTrue(optional.isEmpty());
  }
}
