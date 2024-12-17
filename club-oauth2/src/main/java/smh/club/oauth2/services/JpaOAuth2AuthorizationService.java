package smh.club.oauth2.services;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import smh.club.oauth2.contracts.mappers.AuthorizationMapper;
import smh.club.oauth2.domain.entities.AuthorizationEntity;
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

  /**
   * Saves an {@link OAuth2Authorization} to the database.
   *
   * @param authorization The {@link OAuth2Authorization} to add.
   */
  @Override
  public void save(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");
    var entity = mapper.toEntity(authorization);
    this.authorizationRepository.save(entity);
  }

  /**
   * Removes an {@link OAuth2Authorization} from the database.
   *
   * @param authorization The {@link OAuth2Authorization} to remove.
   */
  @Override
  public void remove(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");
    this.authorizationRepository.deleteById(authorization.getId());
  }

  /**
   * Finds an {@link OAuth2Authorization} by id.
   *
   * @param id The id of the authorization object.
   * @return An {@link OAuth2Authorization}.
   */
  @Override
  public OAuth2Authorization findById(String id) {
    Assert.hasText(id, "id cannot be empty");
    return this.authorizationRepository.findById(id).map(this::toAuth).orElse(null);
  }

  /**
   * Finds an {@link OAuth2Authorization} by either a token value or by token type and token value.
   *
   *<p>
   *   If the OAuth2TokenValue is OAuthParameterNames.State, then search
   *      the Authorization table by the state value.  It should also be unique.<br>
   *
   *   Otherwise, search the token table by the token value.  It should be unique.<br>
   *
   *   return null if no authorization is found.
   *</p>
   *
   * @param token The string value of a token.
   * @param tokenType The {@link OAuth2TokenType} token type.
   * @return An {@link OAuth2Authorization}.or null if no matching authorization is found.
   */
  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Assert.hasText(token, "token cannot be empty");

    if(isStateTokenType(tokenType)) {
      return authorizationRepository.findByState(token).map(this::toAuth).orElse(null);
    } else {
      var tokenEntity = tokenRepository.findByTokenValue(token);
      return tokenEntity.map(t -> toAuth(t.getAuthorization())).orElse(null);
    }
  }

  private boolean isStateTokenType(OAuth2TokenType tokenType) {
    return tokenType != null && OAuth2ParameterNames.STATE.equals(tokenType.getValue());
  }

  private OAuth2Authorization toAuth(AuthorizationEntity entity) {
    // Check a matching client registration exists for the authorization
    Optional.ofNullable(
        registeredClientRepository.findById(entity.getRegisteredClientId())).
        orElseThrow(() -> new DataRetrievalFailureException(
            "The RegisteredClient with id '" + entity.getRegisteredClientId()
                + "' was not found in the RegisteredClientRepository."));

    return this.mapper.toAuthorization(entity);
  }
}
