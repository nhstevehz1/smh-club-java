package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.AuthorizationMapper;
import com.smh.club.oauth2.domain.entities.AuthorizationEntity;
import com.smh.club.oauth2.domain.entities.TokenEntity;
import com.smh.club.oauth2.domain.models.OAuth2AuthorizationEx;
import com.smh.club.oauth2.domain.models.TokenType;
import com.smh.club.oauth2.domain.repos.AuthorizationRepository;
import com.smh.club.oauth2.domain.repos.ClientRepository;
import com.smh.club.oauth2.domain.repos.TokenRepository;
import java.time.Instant;
import java.util.*;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class AuthorizationServiceTests {

  @Mock
  private AuthorizationRepository authRepoMock;

  @Mock
  private ClientRepository clientRepoMock;

  @Mock
  private TokenRepository tokenRepoMock;

  @Mock
  private AuthorizationMapper mapperMock;

  @InjectMocks
  private JpaOAuth2AuthorizationService service;

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.COLLECTION_MAX_SIZE, 2)
          .set(Keys.COLLECTION_MIN_SIZE, 2)
          .set(Keys.JPA_ENABLED, true);

  @Test
  public void save_authorization_authorization_doesnt_exist() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), auth.getRegisteredClientId())
        .create();

    when(authRepoMock.findById(auth.getId())).thenReturn(Optional.empty());
    when(mapperMock.toEntity(auth)).thenReturn(entity);
    when(authRepoMock.save(entity)).thenReturn(entity);

    // execute
    service.save(auth);

    // verify
    verify(authRepoMock).findById(auth.getId());
    verify(authRepoMock).save(entity);
    verify(mapperMock).toEntity(auth);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void save_authorization_authorization_exists() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), auth.getRegisteredClientId())
        .create();

    when(authRepoMock.findById(auth.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.update(auth, entity)).thenReturn(entity);
    when(authRepoMock.save(entity)).thenReturn(entity);

    // execute
    service.save(auth);

    // verify
    verify(authRepoMock).findById(auth.getId());
    verify(authRepoMock).save(entity);
    verify(mapperMock).update(auth, entity);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void remove_authorization() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    doNothing().when(authRepoMock).deleteById(auth.getId());

    // execute
    service.remove(auth);

    // verify
    verify(authRepoMock).deleteById(auth.getId());
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void find_by_id_registered_client_found() {
    // setup
    var client = createClient();
    var auth = createAuthorization(client.getId());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), client.getId())
        .create();
    var id = auth.getId();

    when(authRepoMock.findById(id)).thenReturn(Optional.ofNullable(entity));
    when(clientRepoMock.existsById(auth.getRegisteredClientId())).thenReturn(true);
    when(mapperMock.toAuthorization(entity)).thenReturn(auth);

    // execute
    service.findById(id);

    // verify
    verify(authRepoMock).findById(id);
    verify(clientRepoMock).existsById(auth.getRegisteredClientId());
    verify(mapperMock).toAuthorization(entity);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void find_by_id_registered_client_not_found_throws() {
    // setup
    var regClientId = UUID.randomUUID().toString();
    var auth = createAuthorization(regClientId);
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), regClientId)
        .create();
    var id = auth.getId();

    when(authRepoMock.findById(id)).thenReturn(Optional.ofNullable(entity));
    when(clientRepoMock.existsById(auth.getRegisteredClientId())).thenReturn(false);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class, () -> service.findById(id));

    verify(authRepoMock).findById(id);
    verify(clientRepoMock).existsById(auth.getRegisteredClientId());
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void find_by_token_tokenTye_is_state_returns_auth() {
    // setup
    var client = createClient();
    var auth = createAuthorization(client.getId());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), client.getId())
        .create();
    var state = entity.getState();

    when(authRepoMock.findByState(state)).thenReturn(Optional.of(entity));
    when(clientRepoMock.existsById(auth.getRegisteredClientId())).thenReturn(true);
    when(mapperMock.toAuthorization(entity)).thenReturn(auth);

    var tokenType = new OAuth2TokenType(OAuth2ParameterNames.STATE);

    // execute
    var ret = service.findByToken(state, tokenType);

    assertNotNull(ret);
    verify(authRepoMock).findByState(state);
    verify(clientRepoMock).existsById(auth.getRegisteredClientId());
    verify(mapperMock).toAuthorization(entity);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void find_by_token_tokenTyp_is_state_returns_null() {
    // setup
    when(authRepoMock.findByState(any())).thenReturn(Optional.empty());
    var tokenType = new OAuth2TokenType(OAuth2ParameterNames.STATE);
    var tokenValue = Instancio.create(String.class);

    // execute
    var ret = service.findByToken(tokenValue, tokenType);

    assertNull(ret);
    verify(authRepoMock).findByState(tokenValue);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @ParameterizedTest
  @EnumSource(TokenType.class)
  public void find_by_token_returns_auth(TokenType tokenTypeEnum) {
    // setup
    var client = createClient();
    var auth = createAuthorization(client.getId());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), client.getId())
        .create();

    // token type doesn't matter as long as there is an authorization attached to it.
    var token = Instancio.of(TokenEntity.class)
        .set(field(TokenEntity::getAuthorization), entity)
        .create();

    when(authRepoMock.findByTokensTokenTypeAndTokensTokenValue(tokenTypeEnum, token.getTokenValue()))
        .thenReturn(Optional.of(entity));
    when(clientRepoMock.existsById(auth.getRegisteredClientId())).thenReturn(true);
    when(mapperMock.toAuthorization(entity)).thenReturn(auth);

    var tokenType = new OAuth2TokenType(tokenTypeEnum.getParamName());

    // execute
    var ret = service.findByToken(token.getTokenValue(), tokenType);

    assertNotNull(ret);
    verify(authRepoMock).findByTokensTokenTypeAndTokensTokenValue(tokenTypeEnum, token.getTokenValue());
    verify(clientRepoMock).existsById(auth.getRegisteredClientId());
    verify(mapperMock).toAuthorization(entity);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  @Test
  public void find_by_token_tokenType_is_null_returns_auth() {
    // setup
    var client = createClient();
    var auth = createAuthorization(client.getId());
    var entity = Instancio.of(AuthorizationEntity.class)
        .set(field(AuthorizationEntity::getId), auth.getId())
        .set(field(AuthorizationEntity::getRegisteredClientId), client.getId())
        .create();

    // token type doesn't matter as long as there is an authorization attached to it.
    var token = Instancio.of(TokenEntity.class)
        .set(field(TokenEntity::getAuthorization), entity)
        .create();

    when(authRepoMock.findByTokensTokenValue(token.getTokenValue())).thenReturn(Optional.of(entity));
    when(clientRepoMock.existsById(auth.getRegisteredClientId())).thenReturn(true);
    when(mapperMock.toAuthorization(entity)).thenReturn(auth);

    OAuth2TokenType tokenType = null;

    // execute
    var ret = service.findByToken(token.getTokenValue(), tokenType);

    assertNotNull(ret);
    verify(authRepoMock).findByTokensTokenValue(token.getTokenValue());
    verify(clientRepoMock).existsById(auth.getRegisteredClientId());
    verify(mapperMock).toAuthorization(entity);
    verifyNoMoreInteractions(authRepoMock, mapperMock, tokenRepoMock);
  }

  private OAuth2Authorization createAuthorization(String regClientId) {

    OAuth2Authorization.Builder builder =
        OAuth2AuthorizationEx.withRegisteredClientId(regClientId)
            .id(UUID.randomUUID().toString())
            .principalName("principal")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .attribute(OAuth2ParameterNames.STATE, "state")
            .attribute(OAuth2ParameterNames.STATE, "state")
            .attribute(OAuth2ParameterNames.ACCESS_TOKEN, UUID.randomUUID().toString())
            .attribute(OAuth2ParameterNames.REFRESH_TOKEN, UUID.randomUUID().toString())
            .authorizedScopes(Set.of("scope1", "scope2"));

    createTokens().forEach(builder::token);

    return builder.build();
  }

  private List<OAuth2Token> createTokens() {

    var list = new ArrayList<OAuth2Token>();
    list.add(new OAuth2UserCode("oauth2userCode",
        Instant.now(), Instant.now().plusSeconds(120)));

    list.add(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "oauthAccessToken", Instant.now(),
        Instant.now().plusSeconds(120), Set.of("scope1", "scope2")));

    list.add(new OAuth2RefreshToken("oath2RefreshToken", Instant.now(),
        Instant.now().plusSeconds(120)));

    list.add(new OAuth2AuthorizationCode("oauth2AuthToken", Instant.now(),
        Instant.now().plusSeconds(120)));

    list.add(new OAuth2DeviceCode("oauth2DeviceCode", Instant.now(),
        Instant.now().plusSeconds(120)));

    var oidcToken = OidcIdToken.withTokenValue("oidcToken")
        .tokenValue("oidcToken")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(120))
        .build();
    list.add(oidcToken);

    return list;
  }

  private RegisteredClient createClient() {
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
}
