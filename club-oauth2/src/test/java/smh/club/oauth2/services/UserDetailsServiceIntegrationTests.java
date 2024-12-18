package smh.club.oauth2.services;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import smh.club.oauth2.domain.entities.UserDetailsEntity;
import smh.club.oauth2.domain.repos.UserRepository;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"tests", "prod"})
@Transactional
@ExtendWith(InstancioExtension.class)
// Can't use @DataJpaTest due to some converters need an injected ObjectMapper
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class UserDetailsServiceIntegrationTests {

  @Autowired
  private UserRepository repo;

  @Autowired
  private UserDetailsService service;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE, 3)
          .set(Keys.SET_BACK_REFERENCES, true);

  @Test
  public void LoadUserByUsername_returns_User() {
    // setup
    var users = Instancio.ofList(UserDetailsEntity.class)
        .size(5)
        .ignore(field(UserDetailsEntity::getId))
        .ignore(field(GrantedAuthorityEntity::getId))
        .create();

    var username = users.get(3).getUsername();

    repo.saveAll(users);

    // execute
    var user = service.loadUserByUsername(username);

    // verify
    assertNotNull(user);
  }

  @Test
  public void loadByUseName_throws_when_user_not_found() {
    // setup
    var users = Instancio.ofList(UserDetailsEntity.class)
        .size(5)
        .ignore(field(UserDetailsEntity::getId))
        .ignore(field(GrantedAuthorityEntity::getId))
        .create();

    var username = "XXXTENTACION";

    repo.saveAll(users);

    // execute and verify
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));
  }
}
