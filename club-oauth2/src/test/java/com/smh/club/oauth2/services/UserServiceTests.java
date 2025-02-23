package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.UserDetailsDto;
import com.smh.club.oauth2.dto.UserDto;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UserServiceTests {

  @WithSettings
  Settings settings =
      Settings.create()
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE, 3)
          .set(Keys.SET_BACK_REFERENCES, true);

  @Mock
  private UserRepository repoMock;

  @Mock
  private UserMapper mapperMock;

  @Mock
  private PasswordEncoder encoderMock;

  @InjectMocks
  private UserServiceImpl svc;

  @Test
  public void getUserPage_returns_pagedDto(){
    // setup

  }

  @Test
  public void getUser_returns_optional_userDto() {
    // setup
    var dto = Instancio.create(UserDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    when(repoMock.findById(dto.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.toUserDto(entity)).thenReturn(dto);

    // execute
    var ret = svc.getUser(dto.getId());

    // verify
    assertTrue(ret.isPresent());
    assertEquals(dto, ret.get());

    verify(repoMock).findById(dto.getId());
    verify(mapperMock).toUserDto(entity);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void get_user_returns_empty() {
    // setup
    long id = 100;
    when(repoMock.findById(id)).thenReturn(Optional.empty());

    // execute
    var ret = svc.getUser(id);

    // verify
    assertTrue(ret.isEmpty());

    verify(repoMock).findById(id);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);

  }

  @Test
  public void getUserDetails_returns_optional_userDetailsDto() {
    // setup
    var dto = Instancio.create(UserDetailsDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    when(repoMock.findById(dto.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.toUserDetailsDto(entity)).thenReturn(dto);

    // execute
    var ret = svc.getUserDetails(dto.getId());

    // verify
    assertTrue(ret.isPresent());
    assertEquals(dto, ret.get());

    verify(repoMock).findById(dto.getId());
    verify(mapperMock).toUserDetailsDto(entity);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void createUser_returns_userDetailsDto() {
    // setup
    long id = 100;
    when(repoMock.findById(id)).thenReturn(Optional.empty());

    // execute
    var ret = svc.getUserDetails(id);

    // verify
    assertTrue(ret.isEmpty());

    verify(repoMock).findById(id);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void deleteUser_returns_void() {
    // setup
    long id = 100;
    when(repoMock.existsById(id)).thenReturn(true);
    doNothing().when(repoMock).deleteById(id);

    // execute
    svc.deleteUser(id);

    // verify
    verify(repoMock).deleteById(id);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }

  @Test
  public void updatePassword_returns_void() {
    // setup
    long id = 100;
    var password = Instancio.create(String.class);
    when(repoMock.existsById(id)).thenReturn(true);
    when(encoderMock.encode(anyString())).thenReturn(password);
    doNothing().when(repoMock).updatePassword(id, password);

    // execute
    svc.resetPassword(id);

    // verify
    verify(repoMock).existsById(id);
    verify(encoderMock).encode(anyString());
    verify(repoMock).updatePassword(id, password);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);

  }

  @Test
  public void updatePassword_throws_exception() {
    // setup
    long id = 100;
    when(repoMock.existsById(id)).thenReturn(false);

    // execute
    assertThrows(EntityNotFoundException.class, () -> svc.resetPassword(id));

    // verify
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);

  }

  @Test
  public void updateUserDetails_returns_userDetailsDto() {
    // setup
    var dto = Instancio.create(UserDetailsDto.class);
    var entity = Instancio.create(UserDetailsEntity.class);
    when(repoMock.findById(dto.getId())).thenReturn(Optional.of(entity));
    when(mapperMock.updateUserEntity(dto, entity)).thenReturn(entity);
    when(mapperMock.toUserDetailsDto(entity)).thenReturn(dto);

    // execute
    var ret = svc.updateUserDetails(dto.getId(), dto);

    // verify
    assertTrue(ret.isPresent());
    assertEquals(dto, ret.get());
    verify(repoMock).findById(dto.getId());
    verify(mapperMock).toUserDetailsDto(entity);
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);

  }

  @Test
  public void updateUserDetails_returns_empty() {
    // setup
    var dto  = Instancio.create(UserDetailsDto.class);
    when(repoMock.findById(dto.getId())).thenReturn(Optional.empty());

    // execute
    var ret = svc.updateUserDetails(dto.getId(), dto);

    // verify
    assertTrue(ret.isEmpty());
    verify(repoMock).findById(dto.getId());
    verifyNoMoreInteractions(repoMock, mapperMock, encoderMock);
  }


}
