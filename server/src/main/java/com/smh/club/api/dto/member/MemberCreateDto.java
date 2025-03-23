package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.dto.address.AddressCreateDto;
import com.smh.club.api.dto.email.EmailCreateDto;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
public class MemberCreateDto extends MemberBaseDto {

  @Valid
  @NotNull
  @JsonProperty("addresses")
  private List<AddressCreateDto> addresses;

  @Valid
  @NotNull
  @JsonProperty("phones")
  private List<PhoneCreateDto> phones;

  @Valid
  @NotNull
  @JsonProperty("emails")
  private List<EmailCreateDto> emails;
}
