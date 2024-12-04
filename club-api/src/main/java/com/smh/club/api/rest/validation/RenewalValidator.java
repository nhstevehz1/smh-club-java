package com.smh.club.api.rest.validation;

import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenewalValidator implements ConstraintValidator<ValidRenewal, RenewalDto> {

  @Override
  public boolean isValid(RenewalDto renewal, ConstraintValidatorContext constraintValidatorContext) {

    // we are not validating on null.
    if (renewal == null || renewal.getRenewalDate() == null) {
      return true;
    }

    // Renewal year must be same year as renewal date or for the year early.  Covers late renewals.
    return renewal.getRenewalYear() == renewal.getRenewalDate().getYear()
        || renewal.getRenewalYear() == renewal.getRenewalDate().getYear() - 1;
  }
}
