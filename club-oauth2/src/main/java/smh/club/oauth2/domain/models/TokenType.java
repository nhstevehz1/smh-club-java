package smh.club.oauth2.domain.models;

import lombok.Getter;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

@Getter
public enum TokenType {
  AuthCode(OAuth2AuthorizationCode.class),
  AccessToken(OAuth2AccessToken.class),
  RefreshToken(OAuth2RefreshToken.class),
  IdToken(OidcIdToken.class),
  UserCode(OAuth2UserCode.class),
  DeviceCode(OAuth2DeviceCode.class);

  private final Class<? extends OAuth2Token> clazz;

  TokenType(Class<? extends OAuth2Token> clazz) {
    this.clazz = clazz;
  }
}
