package com.smh.club.api.rest.validation;

import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;

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
