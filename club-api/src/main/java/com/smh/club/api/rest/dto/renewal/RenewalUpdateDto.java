package com.smh.club.api.rest.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RenewalUpdateDto extends RenewalBaseDto {
  @JsonProperty("id")
  private int id;
}
