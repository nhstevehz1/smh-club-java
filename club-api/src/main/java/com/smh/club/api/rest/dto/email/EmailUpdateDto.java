package com.smh.club.api.rest.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@AllArgsConstructor
@SuperBuilder
public class EmailUpdateDto extends EmailBaseDto {
  @JsonProperty("id")
  private int id;

  public EmailUpdateDto() {
    super();
  }
}
