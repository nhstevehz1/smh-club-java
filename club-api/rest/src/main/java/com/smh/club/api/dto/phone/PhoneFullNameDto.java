package com.smh.club.api.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.annotations.SortAlias;
import com.smh.club.api.rest.annotations.SortTarget;
import com.smh.club.api.dto.FullNameDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.smh.club.api.rest.domain.entities.PhoneEntity;

/**
 * DTO for phones. Includes attached member info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@SortTarget(PhoneEntity.class)
public class PhoneFullNameDto extends PhoneBaseDto {

  @JsonProperty("id")
  private int id;

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;

}
