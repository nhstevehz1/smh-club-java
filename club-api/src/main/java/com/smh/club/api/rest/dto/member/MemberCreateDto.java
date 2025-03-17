package com.smh.club.api.rest.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.dto.address.AddressDto;
import com.smh.club.api.rest.dto.email.EmailDto;
import com.smh.club.api.rest.dto.phone.PhoneDto;
import com.smh.club.api.rest.validation.constraints.BirthDate;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@AllArgsConstructor
@ValidMember
public class MemberCreateDto extends MemberDto{

  @JsonProperty("member_number")
  private int memberNumber;

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
