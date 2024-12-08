package smh.club.oauth2.domain.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smh.club.oauth2.domain.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

  Optional<Client> findByClientId(String clientId);
}
