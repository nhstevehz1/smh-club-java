package smh.club.oauth2.services;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;
import smh.club.oauth2.domain.entities.ClientEntity;
import smh.club.oauth2.domain.repos.AuthorizationConsentRepository;
import smh.club.oauth2.domain.repos.ClientRepository;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"tests", "prod"})
@Transactional
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class ConsentServiceIntegrationTests {

  @Autowired
  private AuthorizationConsentRepository consentRepo;

  @Autowired
  private ClientRepository clientRepo;

  @Autowired
  private OAuth2AuthorizationConsentService service;

  @Test
  public void save_success() {
    // setup
    var consent = createAuthConsent();

    // execute
    service.save(consent);

    // verify
    var ret = consentRepo
        .findByRegisteredClientIdAndPrincipalName(consent.getRegisteredClientId(),
            consent.getPrincipalName());

    assertTrue(ret.isPresent());
  }

  @Test
  public void remove_success() {
    // setup
    var consent = createAuthConsent();
    var entity = createAuthConsentEntity(consent);

    consentRepo.save(entity);

    // execute
    service.remove(consent);

    // verify
    var ret = consentRepo
        .findByRegisteredClientIdAndPrincipalName(consent.getRegisteredClientId(),
            consent.getPrincipalName());

    assertTrue(ret.isEmpty());
  }

  @Test
  public void findById_success_returns_authConsent() {
    // setup
    var consent = createAuthConsent();
    var entity = createAuthConsentEntity(consent);
    var client = createClientEntity(consent);

    consentRepo.save(entity);
    clientRepo.save(client);

    // execute
    var ret = service.findById(consent.getRegisteredClientId(), consent.getPrincipalName());

    // verify
    assertNotNull(ret);
    assertEquals(consent, ret);
  }

  @Test
  public void findById_returns_null_when_consent_does_not_exist() {
    // setup
    var consent = createAuthConsent();

    // execute
    var ret = service.findById(consent.getRegisteredClientId(), consent.getPrincipalName());

    // verify
    assertNull(ret);
  }

  @Test
  public void findById_throws_when_registeredClient_does_not_exist() {
    // setup
    var consent = createAuthConsent();
    var entity = createAuthConsentEntity(consent);

    consentRepo.save(entity);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class,
        () -> service.findById(consent.getRegisteredClientId(), consent.getPrincipalName()));
  }

  private OAuth2AuthorizationConsent createAuthConsent() {
    var clientId = UUID.randomUUID().toString();
    var principalName = Instancio.create(String.class);

    return OAuth2AuthorizationConsent.withId(clientId, principalName)
        .authority(new SimpleGrantedAuthority("USER"))
        .build();
  }

  private AuthorizationConsentEntity createAuthConsentEntity(OAuth2AuthorizationConsent consent) {
    var authorities = consent.getAuthorities()
        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

    return
        AuthorizationConsentEntity.builder()
            .registeredClientId(consent.getRegisteredClientId())
            .principalName(consent.getPrincipalName())
            .authorities(authorities)
            .build();
  }

  private ClientEntity createClientEntity(OAuth2AuthorizationConsent consent) {
    return Instancio.of(ClientEntity.class)
        .set(field(ClientEntity::getId), consent.getRegisteredClientId())
        .create();
  }
}
