package smh.club.oauth2.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import smh.club.oauth2.contracts.RegisteredClientMapper;
import smh.club.oauth2.domain.repos.ClientRepository;

@Profile("dev")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

  private final ClientRepository clientRepository;
  private final RegisteredClientMapper mapper;

  @Override
  public void save(RegisteredClient registeredClient) {

    Assert.notNull(registeredClient, "registeredClient cannot be null");
    var clientEntity = mapper.toClientEntity(registeredClient);
    this.clientRepository.saveAndFlush(clientEntity);
  }

  @Override
  public RegisteredClient findById(String id) {

    Assert.hasText(id, "id cannot be empty");
    return this.clientRepository.findById(id).map(mapper::toRegisteredClient).orElse(null);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    Assert.hasText(clientId, "clientId cannot be empty");
    return null;
    //return this.clientRepository.findByClientId(clientId).map(mapper::toRegisteredClient).orElse(null);
  }

}
