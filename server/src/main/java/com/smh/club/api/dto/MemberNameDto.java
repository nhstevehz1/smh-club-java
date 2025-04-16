package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberNameDto {
  @JsonProperty("first_name")
  String firstName;

  @JsonProperty("middle_name")
  String middleName;

  @JsonProperty("last_name")
  String lastName;

  @JsonProperty("suffix")
  String suffix;
}
