package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.ClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

  Optional<ClientEntity> findByClientId(String clientId);
}
