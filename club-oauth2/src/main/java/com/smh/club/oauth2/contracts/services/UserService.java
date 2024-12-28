package com.smh.club.oauth2.contracts.services;

import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.RoleDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import com.smh.club.oauth2.dto.UserDto;
import com.smh.club.oauth2.responses.PagedDto;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserService {
  PagedDto<UserDto> getUserPage(Pageable pageable);
  Optional<UserDto> getUser(long userId);
  Optional<UserDetailsDto> getUserDetails(long userId);
  UserDetailsDto createUser(CreateUserDto createUserDto);
  void deleteUser(long userid);
  void updatePassword(long userId, String password);
  Optional<UserDetailsDto> updateUserDetails(long id, UserDetailsDto userDetailsDto);
  void deleteRole(long userId, long authId);
  Optional<RoleDto> addRole(long userId, RoleDto roleDto);
}
