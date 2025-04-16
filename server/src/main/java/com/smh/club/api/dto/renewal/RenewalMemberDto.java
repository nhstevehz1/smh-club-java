package com.smh.club.api.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortAlias;
import com.smh.club.api.annotations.SortTarget;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.MemberNameDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for renewals. Includes member info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SortTarget(RenewalEntity.class)
public class RenewalMemberDto extends RenewalDto {

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private MemberNameDto fullName;

}
