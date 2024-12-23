package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.TokenEntity;
import com.smh.club.oauth2.domain.models.TokenType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  TokenRepository extends JpaRepository<TokenEntity, String> {
  Optional<TokenEntity> findByTokenValue(String token);
  Optional<TokenEntity> findByTokenTypeAndTokenValue(TokenType tokenType, String tokenValue);
}
