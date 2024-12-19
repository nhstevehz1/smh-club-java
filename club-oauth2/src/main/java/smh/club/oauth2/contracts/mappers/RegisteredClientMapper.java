package smh.club.oauth2.contracts.mappers;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import smh.club.oauth2.domain.entities.ClientEntity;

public interface RegisteredClientMapper {
  RegisteredClient toRegisteredClient(ClientEntity client );
  ClientEntity toClientEntity(RegisteredClient client);
}
