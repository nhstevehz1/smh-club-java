package smh.club.oauth2.mappers;

import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import smh.club.oauth2.contracts.RegisteredClientMapper;
import smh.club.oauth2.domain.entities.Client;

@NoArgsConstructor
@Component
public class RegisteredClientMapperImpl implements RegisteredClientMapper {

  @Override
  public RegisteredClient toRegisteredClient(@NonNull Client client) {
    Assert.notNull(client, "client cannot be null");
    return RegisteredClient.withId(client.getId().toString())
        .clientId(client.getClientId())
        .clientIdIssuedAt(client.getClientIdIssuedAt())
        .clientSecret(client.getClientSecret())
        .clientSecretExpiresAt(client.getClientSecretExpiresAt())
        .clientName(client.getClientName())
        .clientAuthenticationMethods( methods ->  methods.addAll(client.getClientAuthenticationMethods()))
        .authorizationGrantTypes(grantTypes -> grantTypes.addAll(client.getAuthorizationGrantTypes()))
        .redirectUris(redirectUris -> redirectUris.addAll(client.getRedirectUris()))
        .postLogoutRedirectUris(logoutUris -> logoutUris.addAll(client.getPostLogoutRedirectUris()))
        .scopes(scopes -> scopes.addAll(client.getScopes()))
        .clientSettings(client.getClientSettings())
        .tokenSettings(client.getTokenSettings())
        .build();
  }

  @Override
  public Client toClientEntity(@NonNull RegisteredClient registeredClient) {
    Assert.notNull(registeredClient, "registeredClient cannot be null");
    return Client.builder()
        .id(UUID.fromString(registeredClient.getId()))
        .clientId(registeredClient.getClientId())
        .clientIdIssuedAt(registeredClient.getClientIdIssuedAt())
        .clientSecret(registeredClient.getClientSecret())
        .clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
        .clientName(registeredClient.getClientName())
        .clientAuthenticationMethods(registeredClient.getClientAuthenticationMethods())
        .authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes())
        .redirectUris(registeredClient.getRedirectUris())
        .postLogoutRedirectUris(registeredClient.getPostLogoutRedirectUris())
        .scopes(registeredClient.getScopes())
        .clientSettings(registeredClient.getClientSettings())
        .tokenSettings(registeredClient.getTokenSettings())
        .build();
  }
}
