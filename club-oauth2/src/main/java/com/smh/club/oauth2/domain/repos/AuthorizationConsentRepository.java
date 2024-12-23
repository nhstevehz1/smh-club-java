package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.AuthorizationConsentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationConsentRepository
    extends JpaRepository<AuthorizationConsentEntity, AuthorizationConsentEntity.AuthorizationConsentId> {

  Optional<AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId,
                                                                                String principalName);

  void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
