package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.AuthorizationEntity;
import com.smh.club.oauth2.domain.models.TokenType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<AuthorizationEntity, String> {
  Optional<AuthorizationEntity> findByState(String state);

  Optional<AuthorizationEntity>
    findByTokensTokenTypeAndTokensTokenValue(TokenType tokenType, String tokenValue);

  Optional<AuthorizationEntity>
  findByTokensTokenValue(String tokenValue);
}
