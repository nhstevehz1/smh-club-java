package com.smh.club.oauth2.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.contracts.mappers.RegisteredClientMapper;
import com.smh.club.oauth2.domain.entities.ClientEntity;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RegisteredClientMapperImpl extends OAuth2MapperBase implements RegisteredClientMapper {

  private final Map<String, ClientAuthenticationMethod>  authMethodMap =
    Map.of(
        ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
        ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_POST,
        ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_JWT,
        ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(), ClientAuthenticationMethod.PRIVATE_KEY_JWT,
        ClientAuthenticationMethod.NONE.getValue(), ClientAuthenticationMethod.NONE,
        ClientAuthenticationMethod.TLS_CLIENT_AUTH.getValue(), ClientAuthenticationMethod.TLS_CLIENT_AUTH,
        ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH.getValue(), ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH
      );

  private  final Map<String, AuthorizationGrantType> authGrantTypeMap =
      Map.of(
          AuthorizationGrantType.AUTHORIZATION_CODE.getValue(), AuthorizationGrantType.AUTHORIZATION_CODE,
          AuthorizationGrantType.REFRESH_TOKEN.getValue(), AuthorizationGrantType.REFRESH_TOKEN,
          AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(), AuthorizationGrantType.CLIENT_CREDENTIALS
      );

  protected RegisteredClientMapperImpl(ObjectMapper mapper) {
    super(mapper);
  }

  public RegisteredClientMapperImpl() {
    super();
  }

  @Override
  public RegisteredClient toRegisteredClient(@NonNull ClientEntity entity) {
    Assert.notNull(entity, "client entity cannot be null");

    var authMethods = entity.getClientAuthenticationMethods().stream()
        .map(m-> Optional.of(authMethodMap.get(m.getValue())).orElse(m))
        .collect(Collectors.toSet());

    var authGrantTypes = entity.getAuthorizationGrantTypes().stream()
        .map(g -> Optional.of(authGrantTypeMap.get(g.getValue())).orElse(g))
        .collect(Collectors.toSet());


    return RegisteredClient.withId(entity.getId())
        .clientId(entity.getClientId())
        .clientIdIssuedAt(entity.getClientIdIssuedAt())
        .clientSecret(entity.getClientSecret())
        .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
        .clientName(entity.getClientName())
        .clientAuthenticationMethods(methods -> methods.addAll(authMethods))
        .authorizationGrantTypes(gt -> gt.addAll(authGrantTypes))
        .redirectUris(uris -> uris.addAll(entity.getRedirectUris()))
        .postLogoutRedirectUris(uris -> uris.addAll(entity.getPostLogoutRedirectUris()))
        .scopes(scopes-> scopes.addAll(entity.getScopes()))
        .clientSettings(ClientSettings.withSettings(parseMap(entity.getClientSettings())).build())
        .tokenSettings(TokenSettings.withSettings(parseMap(entity.getTokenSettings())).build())
        .build();
  }

  @Override
  public ClientEntity toClientEntity(@NonNull RegisteredClient registeredClient) {
    Assert.notNull(registeredClient, "registeredClient cannot be null");

    return ClientEntity.builder()
        .id(registeredClient.getId())
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
        .clientSettings(writeMap(registeredClient.getClientSettings().getSettings()))
        .tokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()))
        .build();
  }
}
