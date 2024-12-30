package com.smh.club.oauth2.controllers;

import com.smh.club.oauth2.contracts.services.UserService;
import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import com.smh.club.oauth2.dto.UserDto;
import com.smh.club.oauth2.responses.PagedDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
  private final String DEFAULT_SORT = "username";

  private final UserService userSvc;

  @Operation(summary = "Gets a page of users")
  @GetMapping
  public ResponseEntity<PagedDto<UserDto>> page(
      @PageableDefault(sort = {DEFAULT_SORT})
      @ParameterObject Pageable pageable) {

    var page = userSvc.getUserPage(pageable);

    return ResponseEntity.ok(page);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserDto> getPage(@PathVariable Long id) {
    var user = userSvc.getUser(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("{id}/details")
  public ResponseEntity<UserDetailsDto> getDetails(@PathVariable Long id) {
    var user = userSvc.getUserDetails(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDetailsDto> createUser(
      @NotNull @Valid @RequestBody CreateUserDto detailsDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(userSvc.createUser(detailsDto));
  }

  @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDetailsDto> updateUser(
      @PathVariable long id,
      @NotNull @Valid @RequestBody UserDetailsDto detailsDto) {

    var user = userSvc.updateUserDetails(id, detailsDto);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "{id}/pwd")
  public ResponseEntity<Void> resetPassword(@PathVariable long id) {
    userSvc.resetPassword(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value ="{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userSvc.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
