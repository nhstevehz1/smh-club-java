package com.smh.club.api.rest.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortAlias;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.dto.FullNameDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO for emails.  Includes attached member info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@SortTarget(EmailEntity.class)
public class EmailFullNameDto extends EmailBaseDto {

  @JsonProperty("id")
  private int id;

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;

  public EmailFullNameDto() {
    super();
  }
}
