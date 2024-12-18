package smh.club.oauth2.services;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.test.context.ActiveProfiles;
import smh.club.oauth2.contracts.mappers.AuthorizationMapper;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
import smh.club.oauth2.domain.entities.ClientEntity;
import smh.club.oauth2.domain.models.OAuth2AuthorizationEx;
import smh.club.oauth2.domain.models.TokenType;
import smh.club.oauth2.domain.repos.AuthorizationRepository;
import smh.club.oauth2.domain.repos.ClientRepository;
import smh.club.oauth2.domain.repos.TokenRepository;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles({"tests", "prod"})
@Transactional
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class AuthorizationServiceIntegrationTests {

  @Autowired
  private OAuth2AuthorizationService service;

  @Autowired
  private AuthorizationRepository authRepo;

  @Autowired
  private ClientRepository clientRepo;

  @Autowired
  private AuthorizationMapper mapper;
  @Autowired
  private TokenRepository tokenRepository;

  @Test
  public void save_success() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());

    // execute
    service.save(auth);

    // verify
    var ret = authRepo.findById(auth.getId());
    assertTrue(ret.isPresent());
  }

  @Test
  public void save_throws_when_authorization_is_null() {
    // setup
    OAuth2Authorization authorization = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.save(authorization));
  }

  @Test
  public void remove_success() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var entity = createAuthorizationEntity(auth);
    authRepo.save(entity);

    // execute
    service.remove(auth);

    // verify
    var ret = authRepo.findById(auth.getId());
    assertTrue(ret.isEmpty());
  }

  @Test
  public void remove_throws_when_authorization_is_null() {
    // setup
    OAuth2Authorization authorization = null;

    // execute and verify
    assertThrows(Exception.class, () -> service.remove(authorization));
  }

  @Test
  public void findById_returns_authorization() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var clientEntity = createClientEntity(auth);

    authRepo.saveAndFlush(authEntity);
    clientRepo.saveAndFlush(clientEntity);


    // execute
    var ret = service.findById(auth.getId());

    assertNotNull(ret);
  }

  @Test
  public void findById_returns_null_when_authorization_not_found() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());

    // execute
    var ret = service.findById(auth.getId());

    // verify
    assertNull(ret);
  }

  @Test
  public void findById_throws_when_registered_client_not_found() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);

    authRepo.save(authEntity);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class,
        () -> service.findById(auth.getId()));
  }

  @Test
  public void findByToken_returns_authorization_when_token_is_state() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var rc = createClientEntity(auth);
    var tokenType = new OAuth2TokenType(OAuth2ParameterNames.STATE);
    var tokenValue = authEntity.getState();

    authRepo.save(authEntity);
    clientRepo.save(rc);

    // execute
    var ret = service.findByToken(tokenValue, tokenType);

    // verify
    assertNotNull(ret);
  }

  @Test
  public void findByToken_returns_null_when_token_is_state_but_not_found() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var tokenType = new OAuth2TokenType(OAuth2ParameterNames.STATE);
    var tokenValue = authEntity.getState();

    // execute
    var ret = service.findByToken(tokenValue, tokenType);

    // verify
    assertNull(ret);
  }

  @Test
  public void findByToken_throws_when_token_is_state_and_registered_client_not_found() {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var tokenType = new OAuth2TokenType(OAuth2ParameterNames.STATE);
    var tokenValue = authEntity.getState();

    authRepo.save(authEntity);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class,
        () ->service.findByToken(tokenValue, tokenType));

  }

  @ParameterizedTest
  @EnumSource(TokenType.class)
  public void findByToken_returns_authorization_when_tokenType_isNot_null(TokenType tokenTypeEnum) {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var rc = createClientEntity(auth);
    var tokenType = new OAuth2TokenType(tokenTypeEnum.getParamName());
    var tokenValue = Objects.requireNonNull(auth.getToken(tokenTypeEnum.getClazz())).getToken().getTokenValue();

    authRepo.save(authEntity);
    clientRepo.save(rc);

    // execute
    var ret = service.findByToken(tokenValue, tokenType);

    // verify
    assertNotNull(ret);
  }

  @ParameterizedTest
  @EnumSource(TokenType.class)
  public void findByToken_throws_when_tokeType_is_other_and_registered_client_not_found(TokenType tokenTypeEnum) {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var rc = createClientEntity(auth);
    var tokenType = new OAuth2TokenType(tokenTypeEnum.getParamName());
    var tokenValue = Objects.requireNonNull(auth.getToken(tokenTypeEnum.getClazz())).getToken().getTokenValue();

    authRepo.save(authEntity);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class, () -> service.findByToken(tokenValue, tokenType));
  }

  @ParameterizedTest
  @EnumSource(TokenType.class)
  public void findByToken_throws_when_tokeType_is_null_and_registered_client_not_found(TokenType tokenTypeEnum) {
    // setup
    var auth = createAuthorization(UUID.randomUUID().toString());
    var authEntity = createAuthorizationEntity(auth);
    var rc = createClientEntity(auth);
    var tokenType = new OAuth2TokenType(tokenTypeEnum.getParamName());
    var tokenValue = Objects.requireNonNull(auth.getToken(tokenTypeEnum.getClazz())).getToken().getTokenValue();

    authRepo.save(authEntity);

    // execute and verify
    assertThrows(DataRetrievalFailureException.class, () -> service.findByToken(tokenValue, tokenType));
  }

  private ClientEntity createClientEntity(OAuth2Authorization auth) {
    return Instancio.of(ClientEntity.class)
        .set(field(ClientEntity::getId), auth.getRegisteredClientId())
        .create();
  }

  private AuthorizationEntity createAuthorizationEntity(OAuth2Authorization auth) {
    return mapper.toEntity(auth);
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

}
