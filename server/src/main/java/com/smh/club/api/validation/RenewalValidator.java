package com.smh.club.api.validation;

import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.validation.constraints.ValidRenewal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;

@Slf4j
public class RenewalValidator implements ConstraintValidator<ValidRenewal, RenewalDto> {

  @Override
  public boolean isValid(RenewalDto renewal, ConstraintValidatorContext constraintValidatorContext) {

    // we are not validating on null.
    if (renewal == null || renewal.getRenewalDate() == null) {
      return true;
    }

    var zonedRenewal = renewal.getRenewalDate().atZone(ZoneId.systemDefault());

    // Renewal year must be same year as renewal date or for the year early.  Covers late renewals.
    return renewal.getRenewalYear() == zonedRenewal.getYear()
        || renewal.getRenewalYear() == zonedRenewal.getYear() - 1;
  }
}
