package smh.club.oauth2.domain.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.TokenEntity;
import smh.club.oauth2.domain.models.TokenType;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, TokenEntity.TokenTypeId> {

  Optional<TokenEntity> findByTokenValue(String token);
  Optional<TokenEntity> findByTokenTypeAndTokenValue(TokenType tokenType, String token);
}
