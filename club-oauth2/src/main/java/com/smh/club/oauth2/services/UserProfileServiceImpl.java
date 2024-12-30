package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.services.UserProfileService;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.responses.PasswordChangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public PasswordChangeResponse changePassword(long userId, ChangePasswordDto changePasswordDto) {
    var user = userRepo.findById(userId);

    return user.map(u -> {
      if ( passwordEncoder.matches(changePasswordDto.getOldPassword(), user.get().getPassword())) {
        var newPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        userRepo.updatePassword(u.getId(), newPassword);
        return PasswordChangeResponse.success();
      } else {
        return PasswordChangeResponse.noPasswordMatch();
      }
    }).orElse(PasswordChangeResponse.noUserMatch());
  }
}
