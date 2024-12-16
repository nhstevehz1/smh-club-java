package smh.club.oauth2.domain.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

  Optional<ClientEntity> findByClientId(String clientId);
}
