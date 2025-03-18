package com.smh.club.api.rest.dto.address;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AddressCreateDto extends AddressBaseDto {
  public AddressCreateDto() {
    super();
  }
}
