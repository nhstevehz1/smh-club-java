package smh.club.oauth2.domain.models;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

@Getter
public enum TokenType {
  AuthCode(OAuth2AuthorizationCode.class),
  AccessToken(OAuth2AuthorizationCode.class),
  RefreshToken(OAuth2AuthorizationCode.class),
  IdToken(OAuth2AuthorizationCode.class),
  UserCode(OAuth2AuthorizationCode.class),
  DeviceCode(OAuth2AuthorizationCode.class);

  private final Class<? extends OAuth2Token> clazz;

  TokenType(Class<? extends OAuth2Token> clazz) {
    this.clazz = clazz;
  }
}
