package com.smh.club.oauth2.contracts.mappers;

import com.smh.club.oauth2.domain.entities.AuthorizationEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

public interface AuthorizationMapper {
  OAuth2Authorization toAuthorization(AuthorizationEntity entity);
  AuthorizationEntity toEntity(OAuth2Authorization authorization);
  AuthorizationEntity update(OAuth2Authorization authorization, AuthorizationEntity entity);
}
