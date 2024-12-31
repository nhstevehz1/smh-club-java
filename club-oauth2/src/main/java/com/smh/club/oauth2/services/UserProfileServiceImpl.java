package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.contracts.services.UserProfileService;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.dto.ProfileDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserProfileServiceImpl implements UserProfileService {

  private final UserRepository userRepo;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<ProfileDto> updateProfile(String username, ProfileDto profileDto) {
    var user = userRepo.findByUsername(username);

    return user
        .map(u -> userMapper.updateUserEntity(profileDto, u))
        .map(userMapper::toProfileDto);
  }

  @Override
  public Optional<ProfileDto> getProfile(String username) {
    var user = userRepo.findByUsername(username);
    return user.map(userMapper::toProfileDto);
  }

  @Override
  public void changePassword(String username, ChangePasswordDto changePasswordDto) {
    var user = userRepo.findByUsername(username);

    user.map(u -> {
      if ( passwordEncoder.matches(changePasswordDto.getOldPassword(), u.getPassword())) {
        var newPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        userRepo.updatePassword(u.getId(), newPassword);
        return u;
      } else {
        throw new IllegalArgumentException("Wrong password");
      }
    }).orElseThrow(() -> new EntityNotFoundException("Username not found"));
  }
}
