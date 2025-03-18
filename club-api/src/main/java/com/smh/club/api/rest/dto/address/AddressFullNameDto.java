package com.smh.club.api.rest.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortAlias;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.dto.FullNameDto;
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
public class AddressFullNameDto extends AddressBaseDto {

  @JsonProperty("id")
  @SortExclude
  private int id;

  @SortAlias("member.memberNumber")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;
}
