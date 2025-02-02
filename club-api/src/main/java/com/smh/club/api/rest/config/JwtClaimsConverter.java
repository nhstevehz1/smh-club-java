package com.smh.club.api.rest.config;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    var authorities = extractRealmRoles(jwt);
    return new JwtAuthenticationToken(jwt, authorities, getPrincipalFromClaim(jwt));
  }

  private String getPrincipalFromClaim(Jwt jwt) {
    var claimName = "preferred_username";
    return jwt.getClaim(claimName);
  }

  @SuppressWarnings("unchecked")
  private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
    Map<String, Object> resource = jwt.getClaim("realm_access");
    Collection<String> roles;

    if (resource == null
        || (roles = (Collection<String>) resource.get("roles")) == null) {
      return Set.of();
    }
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toSet());
  }
}
