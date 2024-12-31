package com.smh.club.oauth2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
  @NotBlank
  private String username;

  @NotBlank
  private String firstName;

  private String middleName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  private String email;
}
