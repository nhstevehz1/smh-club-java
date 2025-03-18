package com.smh.club.api.rest.dto.phone;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
public class PhoneCreateDto extends PhoneBaseDto {
}
