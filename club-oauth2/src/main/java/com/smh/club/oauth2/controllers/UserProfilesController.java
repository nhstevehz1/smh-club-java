package com.smh.club.oauth2.controllers;

import com.smh.club.oauth2.contracts.services.UserProfileService;
import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.dto.ProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserProfilesController {

  private final UserProfileService profileSvc;

  @GetMapping
  public ResponseEntity<ProfileDto> getProfile(@NotNull Principal principal) {
    var user = profileSvc.getProfile(principal.getName());
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  public ResponseEntity<ProfileDto> updateProfile(
      @NotNull  Principal principal,
      @NotNull @Valid @RequestBody ProfileDto profileDto) {

    var profile = profileSvc.updateProfile(principal.getName(), profileDto);

    return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/pwd")
  public ResponseEntity<Void>  changePassword(
      @NotNull Principal principal,
      @NotNull @Valid @RequestBody ChangePasswordDto changePasswordDto) {

    profileSvc.changePassword(principal.getName(), changePasswordDto);

    return ResponseEntity.ok().build();
  }
}
