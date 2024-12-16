package smh.club.oauth2.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import smh.club.oauth2.contracts.RegisteredClientMapper;
import smh.club.oauth2.domain.repos.ClientRepository;

@RequiredArgsConstructor
@Profile("prod")
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
    var rc = entity.map(mapper::toRegisteredClient).orElse(null);
    return rc;
  }
}
