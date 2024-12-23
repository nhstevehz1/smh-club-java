package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.RegisteredClientMapper;
import com.smh.club.oauth2.domain.entities.ClientEntity;
import com.smh.club.oauth2.domain.repos.ClientRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RegisteredClientServiceTests {

  @Mock
  private ClientRepository repoMock;

  @Mock
  private RegisteredClientMapper mapperMock;

  @InjectMocks
  private JpaRegisteredClientService service;

  @Test
  public void save_success() {
    // setup
    var rc = createRegisteredClient();
    var entity = createClientEntity(rc);

    when(mapperMock.toClientEntity(rc)).thenReturn(entity);
    when(repoMock.save(entity)).thenReturn(entity);

    // execute
    service.save(rc);

    // verify
    verify(mapperMock).toClientEntity(rc);
    verify(repoMock).save(entity);
    verifyNoMoreInteractions(repoMock, mapperMock);
  }

  @Test
  public void findById_returns_client() {
    // setup
    var rc = createRegisteredClient();
    var entity = createClientEntity(rc);

    when(repoMock.findById(rc.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.toRegisteredClient(entity)).thenReturn(rc);

    // execute
    var ret = service.findById(rc.getId());

    // verify
    assertNotNull(ret);
    verify(mapperMock).toRegisteredClient(entity);
    verifyNoMoreInteractions(repoMock, mapperMock);
    verifyNoMoreInteractions(repoMock, mapperMock);
  }

  @Test
  public void findById_returns_null() {
    // setup
    var id = UUID.randomUUID().toString();
    when(repoMock.findById(id)).thenReturn(Optional.empty());

    // execute
    var ret = service.findById(id);

    // verify
    assertNull(ret);
    verify(repoMock).findById(id);
    verifyNoMoreInteractions(repoMock, mapperMock);
  }

  @Test
  public void findBClientId_returns_client() {
    // setup
    var rc = createRegisteredClient();
    var entity = createClientEntity(rc);

    when(repoMock.findByClientId(rc.getClientId())).thenReturn(Optional.of(entity));
    when(mapperMock.toRegisteredClient(entity)).thenReturn(rc);

    // execute
    var ret = service.findByClientId(rc.getClientId());

    // verify
    assertNotNull(ret);
    verify(mapperMock).toRegisteredClient(entity);
    verifyNoMoreInteractions(repoMock, mapperMock);
    verifyNoMoreInteractions(repoMock, mapperMock);
  }

  @Test
  public void findByClientId_returns_null() {
    // setup
    var id = UUID.randomUUID().toString();
    when(repoMock.findByClientId(id)).thenReturn(Optional.empty());

    // execute
    var ret = service.findByClientId(id);

    // verify
    assertNull(ret);
    verify(repoMock).findByClientId(id);
    verifyNoMoreInteractions(repoMock, mapperMock);
  }

  private RegisteredClient createRegisteredClient() {
    return RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("messaging-client")
        .clientSecret("{noop}secret")
        .clientIdIssuedAt(Instant.now())
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
        .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .scope("message.read")
        .scope("message.write")
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();
  }

  private ClientEntity createClientEntity(RegisteredClient rc) {
    return Instancio.of(ClientEntity.class)
        .set(field(ClientEntity::getId), rc.getId())
        .set(field(ClientEntity::getClientId), rc.getClientId())
        .create();
  }
}
