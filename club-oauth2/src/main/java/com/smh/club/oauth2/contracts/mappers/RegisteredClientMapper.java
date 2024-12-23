package com.smh.club.oauth2.contracts.mappers;

import com.smh.club.oauth2.domain.entities.ClientEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface RegisteredClientMapper {
  RegisteredClient toRegisteredClient(ClientEntity client );
  ClientEntity toClientEntity(RegisteredClient client);
}
