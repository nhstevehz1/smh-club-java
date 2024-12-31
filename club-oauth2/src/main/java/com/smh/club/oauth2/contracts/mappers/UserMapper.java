package com.smh.club.oauth2.contracts.mappers;

import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.dto.*;
import java.util.Set;
import org.springframework.data.domain.Page;

public interface  UserMapper {
  UserDetailsDto toUserDetailsDto(UserDetailsEntity userEntity);
  UserDetailsEntity toUserDetailsEntity(CreateUserDto createUserDto);
  UserDto toUserDto(UserDetailsEntity userEntity);
  ProfileDto toProfileDto(UserDetailsEntity userEntity);
  Page<UserDto> toPage(Page<UserDetailsEntity> page);
  UserDetailsEntity updateUserEntity(UserDetailsDto userDetailsDto, UserDetailsEntity userEntity);
  UserDetailsEntity updateUserEntity(ProfileDto profileDto, UserDetailsEntity userEntity);
  RoleDto toRoleDto(GrantedAuthorityEntity grantedEntity);
  Set<RoleDto> toRoleSet(Set<GrantedAuthorityEntity> grantedAuthorities);
  GrantedAuthorityEntity toGrantedAuthorityEntity(RoleDto roleDto);
}
