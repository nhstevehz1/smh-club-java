package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.contracts.services.GrantedAuthorityService;
import com.smh.club.oauth2.domain.repos.GrantedAuthorityRepo;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.RoleDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class GrantedAuthorityServiceImpl implements GrantedAuthorityService {

  private final UserRepository userRepo;
  private final GrantedAuthorityRepo gaRepo;
  private final UserMapper userMapper;

  @Override
  public RoleDto addRole(long userId, RoleDto role) {
    return userRepo.findById(userId)
        .map(u -> {

          var ga = userMapper.toGrantedAuthorityEntity(role);
          ga.setUserDetails(u);
          gaRepo.save(ga);
          return userMapper.toRoleDto(ga);

        }).orElseThrow(
            () -> new EntityNotFoundException("User with id: " + userId + " not found"));
  }

  @Override
  public void deleteRole(long userId, RoleDto role) {
    var user = userRepo.findById(userId);
    if (user.isPresent()) {
      var auth = user.get().getAuthorities()
          .stream()
          .filter(ga -> ga.getId() == role.getId())
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException("Role with id: " + role.getId() + " not found"));

      user.get().getAuthorities().remove(auth);
    } else {
      throw new EntityNotFoundException("User with id: " + userId + " not found");
    }

  }
}
