package com.smh.club.api.rest.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
public class PhoneUpdateDto extends PhoneBaseDto {
  @JsonProperty("id")
  private int id;
}
