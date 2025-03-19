package com.smh.club.api.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.annotations.SortAlias;
import com.smh.club.api.rest.annotations.SortTarget;
import com.smh.club.api.dto.FullNameDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.smh.club.api.rest.domain.entities.AddressEntity;

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
public class AddressFullNameDto extends AddressBaseDto {

  @JsonProperty("id")
  private int id;

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;
}
