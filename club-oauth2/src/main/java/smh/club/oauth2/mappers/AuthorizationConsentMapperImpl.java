package smh.club.oauth2.mappers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.stereotype.Component;
import smh.club.oauth2.contracts.AuthorizationConsentMapper;
import smh.club.oauth2.domain.entities.AuthorizationConsentEntity;

@Component
public class AuthorizationConsentMapperImpl implements AuthorizationConsentMapper {

  @Override
  public OAuth2AuthorizationConsent toAuthConsent(AuthorizationConsentEntity entity) {
    var consent = OAuth2AuthorizationConsent
        .withId(entity.getRegisteredClientId(), entity.getPrincipalName());

    entity.getAuthorities().forEach(a -> consent.authority(new SimpleGrantedAuthority(a)));

    return consent.build();
  }

  @Override
  public AuthorizationConsentEntity toEntity(OAuth2AuthorizationConsent authorizationConsent) {

    var entity = AuthorizationConsentEntity.builder()
        .registeredClientId(authorizationConsent.getRegisteredClientId())
        .principalName(authorizationConsent.getPrincipalName())
        .build();;

    authorizationConsent.getAuthorities()
        .forEach(a -> entity.getAuthorities().add(a.getAuthority()));

    return entity;
  }
}
