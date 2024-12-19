package smh.club.oauth2.services;

import java.util.Optional;
import java.util.UUID;
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
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import smh.club.oauth2.contracts.mappers.AuthorizationConsentMapper;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;
import smh.club.oauth2.domain.repos.AuthorizationConsentRepository;
import smh.club.oauth2.domain.repos.ClientRepository;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class ConsentServiceTests {

  @Mock
  private AuthorizationConsentRepository consentRepoMock;

  @Mock
  private ClientRepository clientRepoMock;

  @Mock
  private AuthorizationConsentMapper mapperMock;

  @InjectMocks
  private JpaOAuth2AuthorizationConsentService service;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.COLLECTION_MAX_SIZE, 2)
          .set(Keys.COLLECTION_MIN_SIZE, 2)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void save_success() {
    // setup
    var entity = createAuthConsentEntity();
    var consent = createAuthConsent(entity.getRegisteredClientId(), entity.getPrincipalName());

    when(mapperMock.toEntity(consent)).thenReturn(entity);
    when(consentRepoMock.save(entity)).thenReturn(entity);

    // execute
    service.save(consent);

    // verify
    verify(mapperMock).toEntity(consent);
    verify(consentRepoMock).save(entity);
    verifyNoMoreInteractions(consentRepoMock, mapperMock, clientRepoMock);
  }

  @Test
  public void save_throws_when_authorization_is_null() {
    // setup
    OAuth2AuthorizationConsent consent = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.save(consent) );
  }

  @Test
  public void remove_success() {
    // setup
    var entity = createAuthConsentEntity();
    var consent = createAuthConsent(entity.getRegisteredClientId(), entity.getPrincipalName());

    doNothing().when(consentRepoMock).deleteByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName());

    // execute
    service.remove(consent);

    // verify
    verify(consentRepoMock).deleteByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName()
    );
    verifyNoMoreInteractions(consentRepoMock, mapperMock, clientRepoMock);
  }

  @Test
  public void remove_throws_when_authorization_is_null() {
    // setup
    OAuth2AuthorizationConsent consent = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.remove(consent) );
  }

  @Test
  public void findById_success_returns_authConsent() {
    // setup
    var entity = createAuthConsentEntity();
    var consent = createAuthConsent(entity.getRegisteredClientId(), entity.getPrincipalName());

    when(consentRepoMock.findByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName()
    )).thenReturn(Optional.of(entity));
    when(clientRepoMock.existsById(consent.getRegisteredClientId())).thenReturn(true);
    when(mapperMock.toAuthConsent(entity)).thenReturn(consent);

    // execute
    var ret = service.findById(consent.getRegisteredClientId(), entity.getPrincipalName());

    // verify
    assertNotNull(ret);
    verify(consentRepoMock).findByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName()
    );
    verify(clientRepoMock).existsById(consent.getRegisteredClientId());
    verify(mapperMock).toAuthConsent(entity);
    verifyNoMoreInteractions(consentRepoMock, mapperMock, clientRepoMock);
  }

  @Test
  public void findById_returns_null_when_consent_does_not_exist() {
    // setup
    var clientId = UUID.randomUUID().toString();
    var principalName = Instancio.create(String.class);

    when(consentRepoMock.findByRegisteredClientIdAndPrincipalName(clientId, principalName))
        .thenReturn(Optional.empty());

    // execute
    var ret = service.findById(clientId, principalName);

    // verify
    assertNull(ret);
    verify(consentRepoMock).findByRegisteredClientIdAndPrincipalName(clientId, principalName);
    verifyNoMoreInteractions(consentRepoMock, mapperMock, clientRepoMock);
  }

  @Test
  public void findById_throws_when_registeredClient_does_not_exist() {
    // setup
    var entity = createAuthConsentEntity();
    var consent = createAuthConsent(entity.getRegisteredClientId(), entity.getPrincipalName());

    when(consentRepoMock.findByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName()
    )).thenReturn(Optional.of(entity));
    when(clientRepoMock.existsById(consent.getRegisteredClientId())).thenReturn(false);

    // execute
   assertThrows(DataRetrievalFailureException.class,
       () -> service.findById(consent.getRegisteredClientId(), entity.getPrincipalName()));

    // verify
    verify(consentRepoMock).findByRegisteredClientIdAndPrincipalName(
        entity.getRegisteredClientId(), entity.getPrincipalName()
    );
    verify(clientRepoMock).existsById(consent.getRegisteredClientId());
    verifyNoMoreInteractions(consentRepoMock, mapperMock, clientRepoMock);
  }

  @Test
  public void findById_throws_when_clientId_is_null() {
    // setup
    String clientId = null;
    var principalName = Instancio.create(String.class);

    // execute and verify
    assertThrows(Exception.class, () -> service.findById(clientId, principalName));
  }

  @Test
  public void findById_throws_when_principalName_is_null() {
    // setup
    var clientId = UUID.randomUUID().toString();
    String principalName = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.findById(clientId, principalName));
  }

  private OAuth2AuthorizationConsent createAuthConsent(String clientId, String principalName) {
    return OAuth2AuthorizationConsent.withId(clientId, principalName)
        .authority(new SimpleGrantedAuthority("ROLE_USER"))
        .scope("read")
        .build();
  }

  private AuthorizationConsentEntity createAuthConsentEntity() {
    return Instancio.of(AuthorizationConsentEntity.class)
        .set(field(AuthorizationConsentEntity::getRegisteredClientId), UUID.randomUUID().toString())
        .create();
  }
}
