package smh.club.oauth2.contracts.mappers;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import smh.club.oauth2.domain.entities.AuthorizationEntity;

public interface AuthorizationMapper {
  OAuth2Authorization toAuthorization(AuthorizationEntity entity);
  AuthorizationEntity toEntity(OAuth2Authorization authorization);
  AuthorizationEntity update(OAuth2Authorization authorization, AuthorizationEntity entity);
}
