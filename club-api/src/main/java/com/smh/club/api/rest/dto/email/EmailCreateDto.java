package com.smh.club.api.rest.dto.email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@SuperBuilder
public class EmailCreateDto extends EmailBaseDto {
  public EmailCreateDto() {
    super();
  }
}
