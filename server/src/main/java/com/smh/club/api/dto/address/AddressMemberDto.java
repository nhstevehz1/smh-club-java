package com.smh.club.api.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortAlias;
import com.smh.club.api.annotations.SortTarget;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.MemberNameDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for addresses. Includes member info.
 * Validation annotations exist as a convenience.
 * The intended use of this DTO is with a controller GET method.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@SortTarget(AddressEntity.class)
public class AddressMemberDto extends AddressDto {

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private MemberNameDto fullName;
}
