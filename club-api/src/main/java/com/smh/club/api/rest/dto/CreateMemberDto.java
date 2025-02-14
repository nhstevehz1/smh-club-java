package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberDto {

  @Valid
  @NotNull
  @JsonProperty("member")
  private MemberDto member;

  @Valid
  @NotNull
  @JsonProperty("address")
  private AddressDto address;

  @Valid
  @NotNull
  @JsonProperty("phone")
  private PhoneDto phone;

  @Valid
  @NotNull
  @JsonProperty("email")
  private EmailDto email;
}
