package com.smh.club.oauth2.contracts.services;

import com.smh.club.oauth2.dto.RoleDto;

public interface GrantedAuthorityService {

  RoleDto addRole(long userId, RoleDto role);
  void deleteRole(long userId, RoleDto role);
}
