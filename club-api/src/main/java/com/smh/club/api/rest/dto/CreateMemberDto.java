package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
  @JsonProperty("addresses")
  private List<AddressDto> addresses;

  @Valid
  @NotNull
  @JsonProperty("phones")
  private List<PhoneDto> phones;

  @Valid
  @NotNull
  @JsonProperty("emails")
  private List<EmailDto> emails;
}
