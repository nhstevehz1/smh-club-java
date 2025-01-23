package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortAlias;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.rest.domain.entities.PhoneType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for phones. Includes attached member info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(PhoneEntity.class)
public class PhoneMemberDto {

  @JsonProperty("id")
  private int id;

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;

  @NotBlank
  @Size(min = 1, max = 5)
  @JsonProperty("country_code")
  private String countryCode;

  @NotBlank
  @Size(min = 10, max = 10)
  @Digits(integer = 10, fraction = 0)
  @JsonProperty("phone_number")
  private String phoneNumber;

  @NotNull
  @JsonProperty("phone_type")
  private PhoneType phoneType;
}
