package com.smh.club.oauth2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {

  private long id;

  @Size(min = 8, max = 30)
  @NotBlank
  private String username;

  @Size(min = 1, max = 30)
  @NotBlank
  private String firstName;

  @Size(min = 1, max = 30)
  private String middleName;

  @Size(min = 1, max = 30)
  @NotBlank
  private String lastName;

  @Size(min = 7, max = 40)
  @NotBlank
  @Email
  private String email;

  @Builder.Default
  private boolean accountNonExpired = true;

  @Builder.Default
  private boolean accountNonLocked = true;

  @Builder.Default
  private boolean credentialsNonExpired = true;

  @Builder.Default
  private boolean enabled = false;

  @Builder.Default
  private Set<RoleDto> roles = new HashSet<>();

}
