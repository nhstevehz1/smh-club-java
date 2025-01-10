package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullNameDto {
  @JsonProperty("first_name")
  String firstName;

  @JsonProperty("middle_name")
  String middleName;

  @JsonProperty("last_name")
  String lastName;

  @JsonProperty("suffix")
  String suffix;

  @JsonGetter("last_first")
  String getFullNameLastFirst() {
    var first = String.join(" ", firstName, middleName);
    var last = String.join(" ", lastName, suffix);
    return String.join(", ", last, first);
  }

  @JsonGetter("first_last")
  String getFullNameFirstLast() {
    return String.join(" ", firstName, middleName, lastName, suffix);
  }
}
