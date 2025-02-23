package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.RegisteredClientMapper;
import com.smh.club.oauth2.domain.repos.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Transactional
@Service
public class JpaRegisteredClientService implements RegisteredClientRepository {

  private final ClientRepository clientRepository;
  private final RegisteredClientMapper mapper;

  @Override
  public void save(RegisteredClient registeredClient) {

    Assert.notNull(registeredClient, "registeredClient cannot be null");
    var clientEntity = mapper.toClientEntity(registeredClient);
    this.clientRepository.save(clientEntity);
  }

  @Override
  public RegisteredClient findById(String id) {

    Assert.hasText(id, "id cannot be empty");
    return this.clientRepository.findById(id).map(mapper::toRegisteredClient).orElse(null);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    Assert.hasText(clientId, "clientId cannot be empty");
    var entity = clientRepository.findByClientId(clientId);
    return entity.map(mapper::toRegisteredClient).orElse(null);
  }
}
