package smh.club.oauth2.domain.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> findByPassword(String password);
  void deleteByUsername(String username);
  boolean existsByUsername(String username);
}
