package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.AuthorizationConsentMapper;
import com.smh.club.oauth2.domain.entities.AuthorizationConsentEntity;
import com.smh.club.oauth2.domain.repos.AuthorizationConsentRepository;
import com.smh.club.oauth2.domain.repos.ClientRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Transactional
@Service
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
  private final AuthorizationConsentRepository authorizationConsentRepository;
  private final ClientRepository clientRepository;
  private final AuthorizationConsentMapper mapper;


  @Override
  public void save(OAuth2AuthorizationConsent authorizationConsent) {
    Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
    this.authorizationConsentRepository
        .save(Objects.requireNonNull(mapper.toEntity(authorizationConsent)));
  }

  @Override
  public void remove(OAuth2AuthorizationConsent authorizationConsent) {
    Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");

    this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
        authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
  }

  @Override
  public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {

    Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
    Assert.hasText(principalName, "principalName cannot be empty");

    return this.authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
        registeredClientId, principalName).map(this::toConsent).orElse(null);
  }

  private OAuth2AuthorizationConsent toConsent(AuthorizationConsentEntity entity) {

    var registeredClientId = entity.getRegisteredClientId();
    var exists = this.clientRepository.existsById(registeredClientId);

    if (!exists) {
      throw new DataRetrievalFailureException(
          "The RegisteredClient with id '" + registeredClientId
              + "' was not found in the RegisteredClientRepository.");
    }

    return mapper.toAuthConsent(entity);
  }
}
