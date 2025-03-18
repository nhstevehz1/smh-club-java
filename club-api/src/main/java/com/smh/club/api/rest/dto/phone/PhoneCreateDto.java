package com.smh.club.api.rest.dto.phone;

import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
@ValidRenewal
public class PhoneCreateDto extends PhoneBaseDto {
}
