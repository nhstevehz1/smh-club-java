package smh.club.oauth2.services;

import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import smh.club.oauth2.domain.entities.UserDetailsEntity;
import smh.club.oauth2.domain.repos.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UserDetailsServiceTests {

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE, 3)
          .set(Keys.SET_BACK_REFERENCES, true);

  @Mock
  private UserRepository repo;

  @InjectMocks
  private JpaUserDetailsService service;

  @Test
  public void LoadUserByUsername_returns_User() {
    // setup
    var user = Instancio.create(UserDetailsEntity.class);
    when(repo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

    // execute
    service.loadUserByUsername(user.getUsername());

    // verify
    verify(repo).findByUsername(user.getUsername());
    verifyNoMoreInteractions(repo);
  }

  @Test
  public void loadUserByUsername_throws_when_username_is_null() {
    // setup
    String username = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.loadUserByUsername(username));
  }

  @Test
  public void loadUserByUsername_throws_when_username_is_empty() {
    // setup
    var username = "";

    // execute and verify
    assertThrows(Exception.class, () -> service.loadUserByUsername(username));
  }

  @Test
  public void loadByUseName_throws_when_user_not_found() {
    // setup
    var username = Instancio.create(String.class);
    when(repo.findByUsername(username)).thenReturn(Optional.empty());

    // execute and verify
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username) );
  }
}
