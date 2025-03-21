package com.smh.club.api.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
public class EmailUpdateDto extends EmailBaseDto {
  @JsonProperty("id")
  private int id;
}
