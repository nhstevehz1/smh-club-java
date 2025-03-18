package com.smh.club.api.rest.dto.renewal;

import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@ValidRenewal
public class RenewalCreateDto extends RenewalBaseDto {}
