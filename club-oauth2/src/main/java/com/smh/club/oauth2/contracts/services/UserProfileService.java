package com.smh.club.oauth2.contracts.services;

import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.responses.PasswordChangeResponse;

public interface UserProfileService {
  PasswordChangeResponse changePassword(long userId, ChangePasswordDto changePasswordDto);
}
