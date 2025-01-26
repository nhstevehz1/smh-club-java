package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.dto.UserDto;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  @PreAuthorize("hasRole('ROLE_app-user')")
  @GetMapping
  public ResponseEntity<UserDto> getUser(JwtAuthenticationToken token) {
    var user = UserDto.builder()
        .username(token.getToken().getClaimAsString(StandardClaimNames.PREFERRED_USERNAME))
        .roles(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
        .build();

    return ResponseEntity.ok(user);
  }

}
