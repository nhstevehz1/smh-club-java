package com.smh.club.api.rest.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.dto.address.AddressDto;
import com.smh.club.api.rest.dto.email.EmailDto;
import com.smh.club.api.rest.dto.phone.PhoneDto;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@ValidMember
public class MemberCreateDto extends MemberBaseDto {

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
