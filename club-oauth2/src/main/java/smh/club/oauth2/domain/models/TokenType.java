package smh.club.oauth2.domain.models;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

@Getter
public enum TokenType {
  AuthCode(OAuth2AuthorizationCode.class, OAuth2ParameterNames.CODE),
  AccessToken(OAuth2AccessToken.class, OAuth2ParameterNames.ACCESS_TOKEN),
  RefreshToken(OAuth2RefreshToken.class, OAuth2ParameterNames.REFRESH_TOKEN),
  IdToken(OidcIdToken.class, OidcParameterNames.ID_TOKEN),
  UserCode(OAuth2UserCode.class, OAuth2ParameterNames.USER_CODE),
  DeviceCode(OAuth2DeviceCode.class, OAuth2ParameterNames.DEVICE_CODE);

  private final Class<? extends OAuth2Token> clazz;
  private final String paramName;

  TokenType(Class<? extends OAuth2Token> clazz, String paramName) {
    this.clazz = clazz;
    this.paramName = paramName;
  }

  public static Optional<TokenType> getByParamName(String paramName) {
    return Stream.of(TokenType.values())
        .filter(t -> t.paramName.equals(paramName))
        .findFirst();
  }
}
