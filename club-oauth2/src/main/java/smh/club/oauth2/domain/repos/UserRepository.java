package smh.club.oauth2.domain.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.AuthUserDetails;

@Repository
public interface UserRepository extends JpaRepository<AuthUserDetails, Long> {
  Optional<AuthUserDetails> findByUsername(String username);
  Optional<AuthUserDetails> findByPassword(String password);
  void deleteByUsername(String username);
  boolean existsByUsername(String username);
}
