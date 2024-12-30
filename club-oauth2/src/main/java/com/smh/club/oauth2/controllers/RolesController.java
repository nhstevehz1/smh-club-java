package com.smh.club.oauth2.controllers;

import com.smh.club.oauth2.contracts.services.GrantedAuthorityService;
import com.smh.club.oauth2.contracts.services.UserService;
import com.smh.club.oauth2.dto.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/users/{userId}/roles")
public class RolesController {

  private final UserService userSvc;
  private final GrantedAuthorityService gaSvc;

  @Operation(summary = "Deletes a user's role")
  @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteRole(@PathVariable long userId, @RequestBody RoleDto roleDto) {

    gaSvc.deleteRole(userId, roleDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Adds a user role.")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RoleDto> addRole(
      @PathVariable long userId,
      @NotNull @Valid @RequestBody RoleDto role) {

    return ResponseEntity.status(HttpStatus.CREATED).body(gaSvc.addRole(userId, role));
  }
}
