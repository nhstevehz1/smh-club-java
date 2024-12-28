package com.smh.club.oauth2.mappers;

import com.smh.club.oauth2.config.mappers.ModelMapperConfig;
import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageImpl;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class UserMapperTests {

  private UserMapperImpl userMapper;

  @WithSettings
  private final Settings settings = Settings.create()
      .set(Keys.SET_BACK_REFERENCES, true)
      .set(Keys.JPA_ENABLED, true)
      .set(Keys.BEAN_VALIDATION_ENABLED, true);

  @BeforeEach
  public void setup() {
    userMapper = new UserMapperImpl(new ModelMapperConfig().createModelMapper());
  }

  @Test
  public void from_userDetailEntity_to_userDetailsDto() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);

    // execute
    var dto = userMapper.toUserDetailsDto(entity);

    // verify
    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getUsername(), dto.getUsername());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getMiddleName(), dto.getMiddleName());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.isAccountNonExpired(), dto.isAccountNonExpired());
    assertEquals(entity.isCredentialsNonExpired(), dto.isCredentialsNonExpired());
    assertEquals(entity.isEnabled(), dto.isEnabled());
    assertEquals(entity.isAccountNonLocked(), dto.isAccountNonLocked());

    entity.getAuthorities().forEach(
        ga -> assertTrue(dto.getRoles().stream().anyMatch(
        r -> ga.getAuthority().equals(r.getRoleName()))));

    dto.getRoles().forEach(
        r -> assertTrue(entity.getAuthorities().stream().anyMatch(
        ga -> ga.getAuthority().equals(r.getRoleName()))));
  }

  @Test
  public void from_createUserDto_to_userDetailsEntity() {
    // setup
    var create = Instancio.of(CreateUserDto.class).ignore(
        field(CreateUserDto::getId))
        .create();

    // execute
    var entity = userMapper.toUserDetailsEntity (create);

    // verify
    assertNotNull(entity);
    assertEquals(create.getId(), entity.getId());
    assertEquals(create.getUsername(), entity.getUsername());
    assertNull(entity.getPassword());
    assertEquals(create.getEmail(), entity.getEmail());
    assertEquals(create.getFirstName(), entity.getFirstName());
    assertEquals(create.getMiddleName(), entity.getMiddleName());
    assertEquals(create.getLastName(), entity.getLastName());
    assertEquals(create.isAccountNonExpired(), entity.isAccountNonExpired());
    assertEquals(create.isCredentialsNonExpired(), entity.isCredentialsNonExpired());
    assertEquals(create.isEnabled(), entity.isEnabled());
    assertEquals(create.isAccountNonLocked(), entity.isAccountNonLocked());

    entity.getAuthorities().forEach(
        ga -> assertTrue(entity.getAuthorities().stream().anyMatch(
        r -> ga.getAuthority().equals(r.getAuthority()))));

    create.getRoles().forEach(
        r -> assertTrue(entity.getAuthorities().stream().anyMatch(
        ga -> ga.getAuthority().equals(r.getRoleName()))));
  }

  @Test
  public void from_userDetailEntity_to_userDto() {
    // setup
    var entity = Instancio.create(UserDetailsEntity.class);

    // execute
    var dto = userMapper.toUserDto(entity);

    // verify
    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getUsername(), dto.getUsername());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getMiddleName(), dto.getMiddleName());
    assertEquals(entity.getLastName(), dto.getLastName());
  }

  @Test
  public void from_userEntity_page_to_userDto_page() {
    // setup
    var entities = Instancio.ofList(UserDetailsEntity.class).size(10).create();
    var entityPage = new PageImpl<>(entities);

    // execute
    var dtoPage = userMapper.toPage(entityPage);

    // verify
    assertNotNull(dtoPage);
    assertEquals(entityPage.getContent().size(), dtoPage.getContent().size());

    entityPage.getContent().forEach(ue -> {
      var dto = dtoPage.getContent().stream().filter(userDto -> userDto.getId() == ue.getId()).findFirst();
      assertTrue(dto.isPresent());
      dto.ifPresent(u ->{
        assertEquals(ue.getId(), u.getId());
        assertEquals(ue.getUsername(), u.getUsername());
        assertEquals(ue.getEmail(), u.getEmail());
        assertEquals(ue.getFirstName(), u.getFirstName());
        assertEquals(ue.getMiddleName(), u.getMiddleName());
        assertEquals(ue.getLastName(), u.getLastName());
      });
    });
  }

  @Test
  public void update_userDetailsEntity_from_userDto() {
    // setup
    var dto = Instancio.create(UserDetailsDto.class);
    var entity = Instancio.of(UserDetailsEntity.class)
        .set(field(UserDetailsEntity::getId), dto.getId())
        .create();
    var id = entity.getId();

    // execute
    var ret = userMapper.updateUserEntity(dto, entity);

    // verify
    assertNotNull(ret);
    assertEquals(dto.getEmail(), ret.getEmail());
    assertEquals(id, ret.getId());
    assertEquals(dto.getUsername(), ret.getUsername());
    assertEquals(dto.getEmail(), ret.getEmail());
    assertEquals(dto.getFirstName(), ret.getFirstName());
    assertEquals(dto.getMiddleName(), ret.getMiddleName());
    assertEquals(dto.getLastName(), ret.getLastName());
    assertEquals(dto.isAccountNonExpired(), ret.isAccountNonExpired());
    assertEquals(dto.isCredentialsNonExpired(), ret.isCredentialsNonExpired());
    assertEquals(dto.isEnabled(), ret.isEnabled());
    assertEquals(dto.isAccountNonLocked(), ret.isAccountNonLocked());

  }

  @Test
  public void from_grantedAuthority_to_roleDto(){
    // setup
    var gaList =
        Instancio.ofList(GrantedAuthorityEntity.class)
            .size(10)
            .create();

    // execute
    gaList.forEach(ga -> {
      var role = userMapper.toRoleDto(ga);

      //verify
      assertNotNull(role);
      assertEquals(ga.getId(), role.getId());
      assertEquals(ga.getAuthority(), role.getRoleName());
    });

  }

  @Test
  public void from_grantedAuthority_to_role_set(){
    //setup
    var gaSet =
        Instancio.ofSet(GrantedAuthorityEntity.class)
            .size(10)
            .create();

    // execute
    var roleSet = userMapper.toRoleSet(gaSet);

    // verify
    assertNotNull(roleSet);
    assertEquals(gaSet.size(), roleSet.size());

    gaSet.forEach(ga ->{
      var role = roleSet.stream().filter(
          r -> r.getId() == ga.getId()).findFirst();

      assertTrue(role.isPresent());
      assertEquals(ga.getAuthority(), role.get().getRoleName());
    });

  }
}
