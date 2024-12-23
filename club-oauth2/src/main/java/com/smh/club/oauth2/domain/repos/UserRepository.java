package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsEntity, String> {
  Optional<UserDetailsEntity> findByUsername(String username);
  void deleteByUsername(String username);
  boolean existsByUsername(String username);

  @Modifying
  @Query("update UserDetailsEntity u set u.password = :password where u.username = :username")
  void updatePassword(@Param("username") String username, @Param("password") String password);
}
