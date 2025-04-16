package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortExclude;
import com.smh.club.api.annotations.SortTarget;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.validation.constraints.BirthDate;
import com.smh.club.api.validation.constraints.ValidMember;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SortTarget(MemberEntity.class)
@ValidMember
public class MemberDto {

  @JsonProperty("id")
  private int id;

  @JsonProperty("member_number")
  @Min(0)
  private int memberNumber;

  @NotBlank
  @JsonProperty("first_name")
  private String firstName;

  @SortExclude
  @JsonProperty("middle_name")
  private String middleName;

  @NotBlank
  @JsonProperty("last_name")
  private String lastName;

  @SortExclude
  @JsonProperty("suffix")
  private String suffix;

  @NotNull
  @BirthDate
  @JsonProperty("birth_date")
  private Instant birthDate;

  @NotNull
  @PastOrPresent
  @JsonProperty("joined_date")
  private Instant joinedDate;
}
