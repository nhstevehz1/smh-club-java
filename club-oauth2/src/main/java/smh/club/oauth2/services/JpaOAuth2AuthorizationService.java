package smh.club.oauth2.services;

import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import smh.club.oauth2.contracts.AuthorizationMapper;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
import smh.club.oauth2.domain.entities.TokenEntity;
import smh.club.oauth2.domain.models.TokenType;
import smh.club.oauth2.domain.repos.AuthorizationRepository;
import smh.club.oauth2.domain.repos.TokenRepository;

@RequiredArgsConstructor
@Profile("prod")
@Transactional
@Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {
  private final AuthorizationRepository authorizationRepository;
  private final RegisteredClientRepository registeredClientRepository;
  private final TokenRepository tokenRepository;
  private final AuthorizationMapper mapper;

  private final Map<String, TokenType> tokenTypeMap =
  Map.of(
      OAuth2ParameterNames.ACCESS_TOKEN, TokenType.AccessToken,
      OAuth2ParameterNames.REFRESH_TOKEN, TokenType.RefreshToken,
      OAuth2ParameterNames.CODE, TokenType.AuthCode,
      OAuth2ParameterNames.USER_CODE, TokenType.UserCode,
      OAuth2ParameterNames.DEVICE_CODE, TokenType.DeviceCode,
      OidcParameterNames.ID_TOKEN, TokenType.IdToken
  );


  @Override
  public void save(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");
    var entity = mapper.toEntity(authorization);
    this.authorizationRepository.save(entity);
  }

  @Override
  public void remove(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");
    this.authorizationRepository.deleteById(authorization.getId());
  }

  @Override
  public OAuth2Authorization findById(String id) {
    Assert.hasText(id, "id cannot be empty");
    return this.authorizationRepository.findById(id).map(this::toAuth).orElse(null);
  }

  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Assert.hasText(token, "token cannot be empty");

    Optional<TokenEntity> result;
    if (tokenType == null) {

      // Try to find using just the token value.  It should be unique
      result = this.tokenRepository.findByTokenValue(token);

    } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {

      // When the a "state" token, query the authorization repo directly
      return this.authorizationRepository.findByState(token).map(this::toAuth).orElse(null);

    } else if (tokenTypeMap.containsKey(tokenType.getValue())){

      // get the mapped TokenType and query on both
      result = this.tokenRepository.findByTokenTypeAndTokenValue(tokenTypeMap.get(tokenType.getValue()), token);

    } else {

      // otherwise return empty
      result = Optional.empty();

    }

    // Map the AuthorizationEntity attached to the token
    return result.map(t -> toAuth(t.getAuthorization())).orElse(null);
  }

  private OAuth2Authorization toAuth(AuthorizationEntity entity) {
    Optional.ofNullable(
        registeredClientRepository.findById(entity.getRegisteredClientId())).
        orElseThrow(() -> new DataRetrievalFailureException(
            "The RegisteredClient with id '" + entity.getRegisteredClientId()
                + "' was not found in the RegisteredClientRepository."));

    return this.mapper.toAuthorization(entity);
  }
}
