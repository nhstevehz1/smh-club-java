package com.smh.club.api.rest.dto.phone;

import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@SuperBuilder
@ValidRenewal
public class PhoneCreateDto extends PhoneBaseDto {
  public PhoneCreateDto() {
    super();
  }
}
