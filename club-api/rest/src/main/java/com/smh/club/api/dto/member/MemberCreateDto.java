package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.dto.address.AddressCreateDto;
import com.smh.club.api.rest.dto.email.EmailCreateDto;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
@ValidMember
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
