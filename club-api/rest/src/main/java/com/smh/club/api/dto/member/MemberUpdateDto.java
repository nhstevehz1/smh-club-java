package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.validation.constraints.ValidMember;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ValidMember
public class MemberUpdateDto extends MemberBaseDto {
  @JsonProperty("id")
  private int id;
}
