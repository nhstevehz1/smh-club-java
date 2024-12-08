package smh.club.oauth2.contracts;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import smh.club.oauth2.domain.entities.Client;

public interface RegisteredClientMapper {
  RegisteredClient toRegisteredClient(Client client );
  Client toClientEntity(RegisteredClient client);
}
