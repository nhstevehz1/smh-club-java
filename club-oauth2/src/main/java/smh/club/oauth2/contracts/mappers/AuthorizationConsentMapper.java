package smh.club.oauth2.contracts.mappers;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;

public interface AuthorizationConsentMapper {
  OAuth2AuthorizationConsent toAuthConsent(AuthorizationConsentEntity entity);
  AuthorizationConsentEntity toEntity(OAuth2AuthorizationConsent authorizationConsent);
}
