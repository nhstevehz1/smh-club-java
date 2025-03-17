package com.smh.club.api.rest.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@AllArgsConstructor
@SuperBuilder
public class PhoneUpdateDto extends PhoneDto {
  @JsonProperty("id")
  private int id;
}
