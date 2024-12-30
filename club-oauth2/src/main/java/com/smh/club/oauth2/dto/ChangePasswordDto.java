package com.smh.club.oauth2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {

  @NotBlank
  private String oldPassword;

  @Size(min = 8, max = 12)
  @NotBlank
  private String newPassword;
}
