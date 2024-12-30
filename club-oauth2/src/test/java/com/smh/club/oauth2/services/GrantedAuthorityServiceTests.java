package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.GrantedAuthorityRepo;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.RoleDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GrantedAuthorityServiceTests {

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE, 3)
          .set(Keys.SET_BACK_REFERENCES, true);

  @Mock
  private UserRepository userRepoMock;

  @Mock
  private GrantedAuthorityRepo gaRepoMock;

  @Mock
  private UserMapper mapperMock;

  @InjectMocks
  private GrantedAuthorityServiceImpl svc;

  @Test
  public void deleteRole_returns_void() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);
    var role = Instancio.create(RoleDto.class);

    entity.addGrantedAuthority(GrantedAuthorityEntity.builder()
            .id(role.getId()).authority(role.getRoleName()).build());


    when(userRepoMock.findById(entity.getId())).thenReturn(Optional.of(entity));

    // execute
    svc.deleteRole(entity.getId(), role);

    // execute
    verify(userRepoMock).findById(entity.getId());
    verifyNoMoreInteractions(gaRepoMock, userRepoMock, mapperMock);
  }

  @Test
  public void deleteRole_user_not_found_throws_exception() {
    // setup
    long userId = 200;
    var role = Instancio.create(RoleDto.class);
    when(userRepoMock.findById(userId)).thenReturn(Optional.empty());

    // execute
    assertThrows(EntityNotFoundException.class, () -> svc.deleteRole(userId, role));

    // execute
    verify(userRepoMock).findById(userId);
    verifyNoMoreInteractions(gaRepoMock, userRepoMock, mapperMock);
  }

  @Test
  public void deleteRole_role_not_found_throws_exception() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);
    var role = Instancio.create(RoleDto.class);

    when(userRepoMock.findById(entity.getId())).thenReturn(Optional.of(entity));

    // execute
    assertThrows(EntityNotFoundException.class, () -> svc.deleteRole(entity.getId(), role));

    // execute
    verify(userRepoMock).findById(entity.getId());
    verifyNoMoreInteractions(gaRepoMock, userRepoMock, mapperMock);
  }

  @Test
  public void addRole_returns_optional_roleDto() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);
    var auth = Instancio.create(GrantedAuthorityEntity.class);
    var dto = Instancio.create(RoleDto.class);
    when(userRepoMock.findById(dto.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.toGrantedAuthorityEntity(dto)).thenReturn(auth);
    when(mapperMock.toRoleDto(auth)).thenReturn(dto);
    when(gaRepoMock.save(auth)).thenReturn(auth);

    // execute
    var ret = svc.addRole(dto.getId(), dto);

    // verify
    assertEquals(dto, ret);
    verify(userRepoMock).findById(dto.getId());
    verify(gaRepoMock).save(auth);
    verify(mapperMock).toGrantedAuthorityEntity(dto);
    verify(mapperMock).toRoleDto(auth);
    verifyNoMoreInteractions(gaRepoMock, userRepoMock, mapperMock);
  }

  @WithMockUser
  @Test
  public void addRole_throws_exception() {
    // setup
    var dto = Instancio.create(RoleDto.class);
    when(userRepoMock.findById(dto.getId())).thenReturn(Optional.empty());

    // execute
    assertThrows(EntityNotFoundException.class, () -> svc.addRole(dto.getId(), dto));

    // verify
    verify(userRepoMock).findById(dto.getId());
    verifyNoMoreInteractions(userRepoMock, mapperMock, userRepoMock);
  }
}
