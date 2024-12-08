package smh.club.oauth2.domain.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.AuthGrantedAuthority;

@Repository
public interface GrantedAuthorityRepository extends JpaRepository<AuthGrantedAuthority, Integer> {
}
