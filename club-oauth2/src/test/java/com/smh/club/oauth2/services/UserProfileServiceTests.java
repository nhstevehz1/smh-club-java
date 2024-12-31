package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.ChangePasswordDto;
import com.smh.club.oauth2.dto.ProfileDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UserProfileServiceTests {

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.BEAN_VALIDATION_ENABLED, true);

  @Mock
  private UserRepository repoMock;

  @Mock
  private UserMapper mapperMock;

  @Mock
  private PasswordEncoder encoderMock;

  @InjectMocks
  private UserProfileServiceImpl svc;

  @Test
  public void get_profile_returns_profile() {
    // setup
    var dto = Instancio.create(ProfileDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(entity.getUsername())).thenReturn(Optional.of(entity));
    when(mapperMock.toProfileDto(entity)).thenReturn(dto);

    // execute
    var ret = svc.getProfile(username);

    // verify
    assertTrue(ret.isPresent());
    assertEquals(dto, ret.get());
    verify(repoMock).findByUsername(username);
    verify(mapperMock).toProfileDto(entity);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void get_profile_returns_empty_profile() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(entity.getUsername())).thenReturn(Optional.empty());

    // execute
    var ret = svc.getProfile(username);

    // verify
    assertTrue(ret.isEmpty());
    verify(repoMock).findByUsername(username);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void update_profile_returns_profile() {
    // setup
    var dto = Instancio.create(ProfileDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(username)).thenReturn(Optional.of(entity));
    when(mapperMock.updateUserEntity(dto, entity)).thenReturn(entity);
    when(mapperMock.toProfileDto(entity)).thenReturn(dto);

    // execute
    var ret = svc.updateProfile(username, dto);

    // verify
    assertTrue(ret.isPresent());
    assertEquals(dto, ret.get());
    verify(repoMock).findByUsername(username);
    verify(mapperMock).updateUserEntity(dto, entity);
    verify(mapperMock).toProfileDto(entity);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void update_profile_returns_empty_profile() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(entity.getUsername())).thenReturn(Optional.empty());

    // execute
    var ret = svc.getProfile(username);

    // verify
    assertTrue(ret.isEmpty());
    verify(repoMock).findByUsername(username);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void change_password_returns_void() {
    // setup
    var dto = Instancio.create(ChangePasswordDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(username)).thenReturn(Optional.of(entity));
    when(encoderMock.matches(dto.getOldPassword(), entity.getPassword())).thenReturn(true);
    when(encoderMock.encode(dto.getNewPassword())).thenReturn(dto.getNewPassword());
    doNothing().when(repoMock).updatePassword(entity.getId(), dto.getNewPassword());

    // execute
    svc.changePassword(username, dto);

    verify(repoMock).findByUsername(username);
    verify(encoderMock).matches(dto.getOldPassword(), entity.getPassword());
    verify(encoderMock).encode(dto.getNewPassword());
    verify(repoMock).updatePassword(entity.getId(), dto.getNewPassword());
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void change_password_when_password_not_matches_throws_illegal_argument_exception() {
    // setup
    var dto = Instancio.create(ChangePasswordDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(username)).thenReturn(Optional.of(entity));
    when(encoderMock.matches(dto.getOldPassword(), entity.getPassword())).thenReturn(false);

    // execute
    assertThrows(IllegalArgumentException.class, () -> svc.changePassword(username, dto));

    verify(repoMock).findByUsername(username);
    verify(encoderMock).matches(dto.getOldPassword(), entity.getPassword());
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void change_password_when_not_exists_throws_entity_not_found_exception() {
    // setup
    var dto = Instancio.create(ChangePasswordDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    var username = entity.getUsername();

    when(repoMock.findByUsername(username)).thenReturn(Optional.empty());

    // execute
    assertThrows(EntityNotFoundException.class, () -> svc.changePassword(username, dto));

    verify(repoMock).findByUsername(username);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }
}
