package com.smh.club.api.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortAlias;
import com.smh.club.api.annotations.SortTarget;
import com.smh.club.api.dto.FullNameDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.smh.club.api.domain.entities.EmailEntity;

/**
 * DTO for emails.  Includes attached member info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@SortTarget(EmailEntity.class)
public class EmailFullNameDto extends EmailDto {

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;
}
