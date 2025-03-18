package com.smh.club.api.rest.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RenewalUpdateDto extends RenewalBaseDto {
  @JsonProperty("id")
  private int id;

  public RenewalUpdateDto() {
    super();
  }
}
