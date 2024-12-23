package com.smh.club.oauth2.domain.models;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.Assert;

public class OAuth2AuthorizationEx extends OAuth2Authorization {

  public static Builder withRegisteredClientId(String clientId) {
    Assert.notNull(clientId, "clientId must not be null");
    return new BuilderEx(clientId);
  }

  public static class BuilderEx extends OAuth2Authorization.Builder {
    public BuilderEx(String clientId) {
      super(clientId);
    }
  }
}
