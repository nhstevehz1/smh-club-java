package smh.club.oauth2.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import org.instancio.Instancio;
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
import smh.club.oauth2.helpers.OAuth2MapperTestBase;
import smh.club.oauth2.helpers.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(InstancioExtension.class)
public class AuthorizationMapperTests extends OAuth2MapperTestBase {

  private AuthorizationMapperImpl mapper;

  private final ObjectMapper objMapper = TestUtils.getObjectMapper();

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true);

  @BeforeEach
  public void setup() {
    mapper = new AuthorizationMapperImpl(TestUtils.getObjectMapper());
  }

  @Test
  public void from_auth_entity_to_authorization() {
    // setup
    var id = UUID.randomUUID().toString();
    var attribs = writeMap(createAttributes());

    var entity = AuthorizationEntity.builder()
        .id(id)
        .registeredClientId("client_id")
        .state("state")
        .principalName("principal")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .attributes(attribs)
        .build();

    entity.getAuthorizedScopes().addAll(List.of("scope1", "scope2"));


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

    entity.getTokens().forEach(t ->
        verifyToken(t, Objects.requireNonNull(auth.getToken(t.getTokenType().getClazz()))));
  }

  private TokenEntity createTokenEntity(TokenType tokenType) {

    var metadataMap = new HashMap<String, Object>();
    metadataMap.put("string", "string");
    metadataMap.put("boolean", true);
    metadataMap.put("integer", 1);

    var token = TokenEntity.builder()
        .tokenType(tokenType)
        .tokenValue(tokenType.name())
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(60))
        .metadata(writeMap(metadataMap))
        .build();

    if (tokenType == TokenType.AccessToken) {
      token.setScopes(Set.of("scope1", "scope2"));
    }

    if (tokenType == TokenType.IdToken) {
      var map = new HashMap<String, Object>();
      map.put("string", "string");
      map.put("boolean", true);
      map.put("integer", 1);
      map.put("state", "state");
      token.setClaims(writeMap(Collections.unmodifiableMap(map)));
    }

    return token;
  }

  private Map<String, Object> createAttributes() {
    Map<String, Object> map = new HashMap<>();
    map.put("string", "string");
    map.put("boolean", true);
    map.put("integer", 1);
    return Collections.unmodifiableMap(map);
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
      assertEquals(expected.getClaims(), writeMap(idToken.getClaims()));
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
    assertEquals(parseMap(entity.getAttributes()), auth.getAttributes());

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
      assertEquals(token.getClaims(), parseMap(actual.getClaims()));
    }
  }

  private Optional<TokenEntity> getToken(TokenType type, AuthorizationEntity entity) {
    return entity.getTokens().stream()
        .filter(t -> t.getTokenType() == type).findFirst();
  }

  private List<OAuth2Token> createTokens(){

    var list = new ArrayList<OAuth2Token>();
    var issuedAt = Instancio.create(Instant.class);
    var expiresAt = issuedAt.plusSeconds(120);

    list.add(new OAuth2UserCode("oauth2userCode", issuedAt, expiresAt));

    list.add(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "oauthAccessToken",
        issuedAt, expiresAt, Set.of("scope1", "scope2")));

    list.add(new OAuth2RefreshToken("oath2RefreshToken", issuedAt, expiresAt));

    list.add(new OAuth2AuthorizationCode("oauth2AuthToken", issuedAt, expiresAt));

    list.add(new OAuth2DeviceCode("oauth2DeviceCode", issuedAt, expiresAt));

    var oidcToken = OidcIdToken.withTokenValue("oidcToken")
        .tokenValue("oidcToken")
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .build();
    list.add(oidcToken);

    return list;
  }
}
