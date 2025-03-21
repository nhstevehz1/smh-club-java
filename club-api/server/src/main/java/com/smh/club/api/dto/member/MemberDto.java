package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortTarget;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.smh.club.api.domain.entities.MemberEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@SortTarget(MemberEntity.class)
public class MemberDto extends MemberBaseDto {

  @JsonProperty("id")
  private int id;
}
