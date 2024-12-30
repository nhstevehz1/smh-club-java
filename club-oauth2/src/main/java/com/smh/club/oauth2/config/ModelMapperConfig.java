package com.smh.club.oauth2.config;

import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.RoleDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper createModelMapper() {
    var modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    userSettings(modelMapper);
    grantedAuthoritySettings(modelMapper);
    return modelMapper;
  }

  private void userSettings(ModelMapper modelMapper) {
    TypeMap<CreateUserDto, UserDetailsEntity> createTypeMap
        = modelMapper.createTypeMap(CreateUserDto.class, UserDetailsEntity.class);

    createTypeMap.addMappings(
        m -> m.skip(UserDetailsEntity::setAuthorities));

    TypeMap<UserDetailsEntity, UserDetailsDto> userTypeMap
        = modelMapper.createTypeMap(UserDetailsEntity.class, UserDetailsDto.class);
    userTypeMap.addMappings(
        m -> m.skip(UserDetailsDto::setRoles));
  }

  private void grantedAuthoritySettings(ModelMapper modelMapper) {
    TypeMap<RoleDto, GrantedAuthorityEntity> entityTypeMap
        = modelMapper.createTypeMap(RoleDto.class, GrantedAuthorityEntity.class);

    entityTypeMap.addMappings(
        m -> {
          m.skip(GrantedAuthorityEntity::setId);
          m.skip(GrantedAuthorityEntity::setUserDetails);
          m.map(RoleDto::getRoleName, GrantedAuthorityEntity::setAuthority);
        });

    TypeMap<GrantedAuthorityEntity, RoleDto> dtoTypeMap
        = modelMapper.createTypeMap(GrantedAuthorityEntity.class, RoleDto.class);

    dtoTypeMap.addMappings(
        m -> m.map(GrantedAuthorityEntity::getAuthority, RoleDto::setRoleName));
  }
}
