package smh.club.oauth2.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.stereotype.Component;
import smh.club.oauth2.contracts.mappers.AuthorizationMapper;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
import smh.club.oauth2.domain.entities.TokenEntity;
import smh.club.oauth2.domain.models.OAuth2AuthorizationEx;
import smh.club.oauth2.domain.models.TokenType;

@RequiredArgsConstructor
@Component
public class AuthorizationMapperImpl implements AuthorizationMapper {

  private final ObjectMapper objMapper;


  @Override
  public OAuth2Authorization toAuthorization(AuthorizationEntity entity) {
    OAuth2Authorization.Builder builder = OAuth2AuthorizationEx.withRegisteredClientId(entity.getRegisteredClientId())
        .id(entity.getId())
        .principalName(entity.getPrincipalName())
        .authorizationGrantType(entity.getAuthorizationGrantType())
        .authorizedScopes(entity.getAuthorizedScopes());

    parseMap(entity.getAttributes()).forEach(builder::attribute);

    if(entity.getState() != null) {
      builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
    }

    entity.getTokens().forEach(t -> {
      switch (t.getTokenType()) {
        case IdToken -> addIdToken(builder, t);
        case AccessToken -> addAccessToken(builder, t);
        case AuthCode -> addAuthCodeToken(builder, t);
        case RefreshToken -> addRefreshToken(builder, t);
        case UserCode -> addUserCodeToken(builder, t);
        case DeviceCode -> addDeviceCodeToken(builder, t);
      }
    });

    return builder.build();
  }

  private void addAuthCodeToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OAuth2AuthorizationCode(
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt());
    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  private void addAccessToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OAuth2AccessToken (
        OAuth2AccessToken.TokenType.BEARER, // only one possible value
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt(),
        entity.getScopes());

    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  private void addRefreshToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OAuth2RefreshToken (
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt());

    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  private void addIdToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OidcIdToken(
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt(),
        entity.getClaims());
    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  private void addUserCodeToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OAuth2UserCode(
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt());

    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  private void addDeviceCodeToken(OAuth2Authorization.Builder builder, TokenEntity entity) {
    var token = new OAuth2DeviceCode(
        entity.getTokenValue(),
        entity.getIssuedAt(),
        entity.getExpiresAt());

    builder.token(token, metadata -> metadata.putAll(entity.getMetadata()));
  }

  @Override
  public AuthorizationEntity toEntity(OAuth2Authorization auth) {

    var entity = AuthorizationEntity.builder()
        .id(auth.getId())
        .registeredClientId(auth.getRegisteredClientId())
        .principalName(auth.getPrincipalName())
        .authorizationGrantType(auth.getAuthorizationGrantType())
        .authorizedScopes(auth.getAuthorizedScopes())
        .attributes(writeMap(auth.getAttributes()))
        .state(auth.getAttribute(OAuth2ParameterNames.STATE))
        .build();

    Arrays.stream(TokenType.values()).forEach(tokenType -> {
      var tokenEntity = mapToken(auth.getToken(tokenType.getClazz()), tokenType);
      tokenEntity.ifPresent(entity::addTokenEntity);
    });

    return entity;
  }

  @Override
  public AuthorizationEntity update(OAuth2Authorization auth, AuthorizationEntity entity) {
    // only the token values should have changed
    Arrays.stream(TokenType.values()).forEach(tokenType -> {
      // map the token to TokenEntity
      var tokenEntity = mapToken(auth.getToken(tokenType.getClazz()), tokenType);

      // Continue if the token exists in the authorization
      tokenEntity.ifPresent(updated -> {
        // get the matching token in the original Authorization entity
        var original = entity.getTokens().stream().
            filter(t -> t.getTokenType() == tokenType).findFirst();
        // if the original AuthorizationEntity contains the token type, then update it.
        // Otherwise, add the token to the AuthenticationEntity
        original.ifPresent(entity::removeTokenEntity);
        entity.addTokenEntity(updated);
      });
    });

    return entity;
  }

  private <T extends OAuth2Token> Optional<TokenEntity> mapToken(OAuth2Authorization.Token<T> token,
                                                                 TokenType tokenType) {
    if (token != null) {
      var entity = TokenEntity.builder()
          .tokenType(tokenType)
          .build();

      setTokenValues(
          token,
          entity::setTokenValue,
          entity::setIssuedAt,
          entity::setExpiresAt,
          entity::setMetadata);

      if (token.getToken() instanceof OAuth2AccessToken accessToken) {
        var scopes = accessToken.getScopes();
        entity.setScopes(scopes);
      } else if (token.getToken() instanceof OidcIdToken idToken) {
        var claims = idToken.getClaims();
        entity.setClaims(claims);
      }

      return Optional.of(entity);

    } else {
      return Optional.empty();
    }
  }

  private void setTokenValues(
      OAuth2Authorization.Token<?> token,
      Consumer<String> tokenValueConsumer,
      Consumer<Instant> issuedAtConsumer,
      Consumer<Instant> expiresAtConsumer,
      Consumer<Map<String, Object>> metadataConsumer) {

  if (token != null) {
      OAuth2Token oAuth2Token = token.getToken();
      tokenValueConsumer.accept(oAuth2Token.getTokenValue());
      issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
      expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
      metadataConsumer.accept(token.getMetadata());
    }
  }

  private String writeMap(Map<String, Object> map) {
    try {
      return objMapper.writeValueAsString(map);
    } catch (JsonProcessingException ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  private Map<String, Object> parseMap(String data) {
    try {
      return this.objMapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }
}
