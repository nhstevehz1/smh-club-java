package smh.club.oauth2.mappers;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
import smh.club.oauth2.domain.entities.TokenEntity;
import smh.club.oauth2.domain.models.OAuth2AuthorizationEx;
import smh.club.oauth2.domain.models.TokenType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(InstancioExtension.class)
public class AuthorizationTests {

  private AuthorizationMapperImpl mapper;

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setup() {
    mapper = new AuthorizationMapperImpl();
  }

  @Test
  public void from_auth_entity_to_authorization() {
    // setup
    var id = UUID.randomUUID().toString();
    var entity = AuthorizationEntity.builder()
        .id(id)
        .registeredClientId(UUID.randomUUID().toString())
        .state("state")
        .principalName("principal")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .build();

    entity.getAuthorizedScopes().addAll(List.of("scope1", "scope2"));

    entity.getAttributes().put("state", "state");

    Stream.of(TokenType.values())
        .forEach(t -> entity.addTokenEntity(createTokenEntity(t)));

    // execute
    var auth = mapper.toAuthorization(entity);

    // verify
    assertNotNull(auth);
    assertEquals(entity.getId(), auth.getId());
    assertEquals(entity.getRegisteredClientId(), auth.getRegisteredClientId());
    assertEquals(entity.getPrincipalName(), auth.getPrincipalName());
    assertEquals(entity.getAuthorizationGrantType(), auth.getAuthorizationGrantType());
    assertEquals(entity.getState(), auth.getAttributes().get(OAuth2ParameterNames.STATE));
    assertEquals(entity.getAttributes(), auth.getAttributes());

    entity.getTokens().forEach(t ->
        verifyToken(t, Objects.requireNonNull(auth.getToken(t.getTokenType().getClazz()))));
  }

  private TokenEntity createTokenEntity(TokenType tokenType) {

    var token = TokenEntity.builder()
        .tokenType(tokenType)
        .tokenValue(tokenType.name())
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(60))
        .build();

    if (tokenType == TokenType.AccessToken) {
      token.setScopes(Set.of("scope1", "scope2"));
    }

    if (tokenType == TokenType.IdToken) {
      var map = new HashMap<String, Object>();
      map.put("string", "string");
      map.put("boolean", true);
      map.put("integer", 1);
      token.setClaims(map);
    }

    return token;
  }

  private <T extends OAuth2Token> void verifyToken(TokenEntity expected, OAuth2Authorization.Token<T> token) {
    var actual = token.getToken();
    assertNotNull(actual);
    assertEquals(expected.getIssuedAt(), Objects.requireNonNull(actual.getIssuedAt()));
    assertEquals(expected.getExpiresAt(), Objects.requireNonNull(actual.getExpiresAt()));
    assertEquals(expected.getTokenValue(), actual.getTokenValue());

    if (actual instanceof OAuth2AccessToken accessToken) {
      assertEquals(expected.getScopes(), accessToken.getScopes());
    }

    if (actual instanceof OidcIdToken idToken) {
      assertEquals(expected.getClaims(), idToken.getClaims());
    }
  }



  @Test
  public void from_authorization_to_entity() {
    // setup
    OAuth2Authorization.Builder builder = OAuth2AuthorizationEx.withRegisteredClientId(UUID.randomUUID().toString())
        .id(UUID.randomUUID().toString())
        .principalName("principal")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .attribute(OAuth2ParameterNames.STATE, "state")
        .attribute(OAuth2ParameterNames.ACCESS_TOKEN, UUID.randomUUID().toString())
        .attribute(OAuth2ParameterNames.REFRESH_TOKEN, UUID.randomUUID().toString())
        .authorizedScopes(Set.of("scope1", "scope2"));

    createTokens().forEach(builder::token);

    var auth = builder.build();

    // execute
    var entity = mapper.toEntity(auth);

    // verify
    assertNotNull(entity);
    assertEquals(entity.getId(), auth.getId());
    assertEquals(entity.getRegisteredClientId(), auth.getRegisteredClientId());
    assertEquals(entity.getPrincipalName(), auth.getPrincipalName());
    assertEquals(entity.getAuthorizationGrantType(), auth.getAuthorizationGrantType());
    assertEquals(entity.getState(), auth.getAttributes().get(OAuth2ParameterNames.STATE));
    assertEquals(entity.getAttributes(), auth.getAttributes());

    Stream.of(TokenType.values()).forEach(t -> {
      var token = getToken(t, entity).orElseThrow();
      verifyToken(Objects.requireNonNull(auth.getToken(t.getClazz())), token);
    });
  }

  private <T extends OAuth2Token> void verifyToken(OAuth2Authorization.Token<T> expected, TokenEntity actual) {
    assertEquals(expected.getToken().getTokenValue(), actual.getTokenValue());
    assertEquals(Objects.requireNonNull(expected.getToken().getIssuedAt()), actual.getIssuedAt());
    assertEquals(Objects.requireNonNull(expected.getToken().getExpiresAt()), actual.getExpiresAt());

    if (expected.getToken() instanceof OAuth2AccessToken token) {
      assertEquals(token.getScopes(), actual.getScopes());
    }

    if (expected.getToken() instanceof OidcIdToken token) {
      assertEquals(token.getClaims(), actual.getClaims());
    }
  }

  private Optional<TokenEntity> getToken(TokenType type, AuthorizationEntity entity) {
    return entity.getTokens().stream()
        .filter(t -> t.getTokenType() == type).findFirst();
  }

  private List<OAuth2Token> createTokens(){

    var list = new ArrayList<OAuth2Token>();
    list.add(new OAuth2UserCode("oauth2userCode",
        Instant.now(), Instant.now().plusSeconds(120)));

    list.add(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "oauthAccessToken", Instant.now(),
        Instant.now().plusSeconds(120), Set.of("scope1", "scope2")));

    list.add(new OAuth2RefreshToken("oath2RefreshToken", Instant.now(),
        Instant.now().plusSeconds(120)));

    list.add(new OAuth2AuthorizationCode("oauth2AuthToken", Instant.now(),
        Instant.now().plusSeconds(120) ));

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
