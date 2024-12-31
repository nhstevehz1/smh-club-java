package com.smh.club.oauth2.mappers;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.dto.*;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapperImpl implements UserMapper {

  private final ModelMapper modelMapper;

  @Override
  public UserDetailsDto toUserDetailsDto(UserDetailsEntity userEntity) {
    var dto = modelMapper.map(userEntity, UserDetailsDto.class);
    dto.setRoles(toRoleSet(userEntity.getAuthorities()));
    return dto;
  }

  @Override
  public UserDetailsEntity toUserDetailsEntity(CreateUserDto createUserDto) {
    var entity = modelMapper.map(createUserDto, UserDetailsEntity.class);
    createUserDto.getRoles().forEach(
        a -> entity.addGrantedAuthority(modelMapper.map(a, GrantedAuthorityEntity.class)));
    return entity;
  }

  @Override
  public UserDto toUserDto(UserDetailsEntity userEntity) {
    return modelMapper.map(userEntity, UserDto.class);
  }

  @Override
  public ProfileDto toProfileDto(UserDetailsEntity userEntity) {
    return modelMapper.map(userEntity, ProfileDto.class);
  }

  @Override
  public Page<UserDto> toPage(Page<UserDetailsEntity> page) {
    return page.map(this::toUserDto);
  }

  @Override
  public UserDetailsEntity updateUserEntity(UserDetailsDto userDetailsDto, UserDetailsEntity userEntity) {
    modelMapper.map(userDetailsDto, userEntity);

    while (!userEntity.getAuthorities().isEmpty()) {
      var auth = userEntity.getAuthorities().iterator().next();
      userEntity.removeGrantedAuthority(auth);
    }

    userDetailsDto.getRoles().forEach(
        r -> userEntity.addGrantedAuthority(modelMapper.map(r, GrantedAuthorityEntity.class)));
    return userEntity;
  }

  @Override
  public UserDetailsEntity updateUserEntity(ProfileDto profileDto, UserDetailsEntity userEntity) {
    modelMapper.map(profileDto, userEntity);
    return userEntity;
  }

  @Override
  public RoleDto toRoleDto(GrantedAuthorityEntity grantedEntity) {
    return modelMapper.map(grantedEntity, RoleDto.class);
  }

  @Override
  public Set<RoleDto> toRoleSet(Set<GrantedAuthorityEntity> grantedAuthorities) {
    return grantedAuthorities
        .stream()
        .map(this::toRoleDto)
        .collect(Collectors.toSet());
  }

  @Override
  public GrantedAuthorityEntity toGrantedAuthorityEntity(RoleDto roleDto) {
    return modelMapper.map(roleDto, GrantedAuthorityEntity.class);
  }

}
