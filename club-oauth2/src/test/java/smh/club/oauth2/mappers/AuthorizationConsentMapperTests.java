package smh.club.oauth2.mappers;

import java.util.stream.Collectors;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(InstancioExtension.class)
public class AuthorizationConsentMapperTests {

  private AuthorizationConsentMapperImpl mapper;

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setup() {
    mapper = new AuthorizationConsentMapperImpl();
  }

  @Test
  public void from_entity_to_consent() {
    // setup
    var entity = Instancio.create(AuthorizationConsentEntity.class);

    // execute
    var consent = mapper.toAuthConsent(entity);

    // verify
    assertEquals(entity.getPrincipalName(), consent.getPrincipalName());
    assertEquals(entity.getRegisteredClientId(), consent.getRegisteredClientId());

    var authStrings = consent.getAuthorities()
        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

    assertEquals(entity.getAuthorities(), authStrings);

  }

  @Test
  public void from_consent_to_entity() {
    // setup
    var builder = OAuth2AuthorizationConsent
        .withId(Instancio.create(String.class), Instancio.create(String.class));
    var auths = Instancio.createSet(SimpleGrantedAuthority.class);
    builder.authorities(authorities -> authorities.addAll(auths));
    var consent = builder.build();

    // execute
    var entity = mapper.toEntity(consent);

    // verify
    assertEquals(consent.getPrincipalName(), entity.getPrincipalName());
    assertEquals(consent.getRegisteredClientId(), entity.getRegisteredClientId());

    var authStrings =
        consent.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    assertEquals(entity.getAuthorities(), authStrings);

  }
}
