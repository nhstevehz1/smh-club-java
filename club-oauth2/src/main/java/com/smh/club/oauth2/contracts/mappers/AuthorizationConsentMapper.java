package com.smh.club.oauth2.contracts.mappers;

import com.smh.club.oauth2.domain.entities.AuthorizationConsentEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;

public interface AuthorizationConsentMapper {
  OAuth2AuthorizationConsent toAuthConsent(AuthorizationConsentEntity entity);
  AuthorizationConsentEntity toEntity(OAuth2AuthorizationConsent authorizationConsent);
}
