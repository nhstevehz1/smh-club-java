package com.smh.club.api.dto.renewal;

import com.smh.club.api.validation.constraints.ValidRenewal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ValidRenewal
public class RenewalCreateDto extends RenewalBaseDto {
}
