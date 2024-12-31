package com.smh.club.oauth2.contracts.services;

import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.dto.ProfileDto;
import java.util.Optional;

public interface UserProfileService {
  Optional<ProfileDto> getProfile(String username);
  Optional<ProfileDto> updateProfile(String username,  ProfileDto profileDto);
  void changePassword(String username, ChangePasswordDto changePasswordDto);
}
