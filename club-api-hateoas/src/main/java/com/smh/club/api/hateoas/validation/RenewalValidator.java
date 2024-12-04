package com.smh.club.api.hateoas.validation;

import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.validation.constraints.ValidRenewal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenewalValidator implements ConstraintValidator<ValidRenewal, RenewalModel> {

  @Override
  public boolean isValid(RenewalModel renewal, ConstraintValidatorContext constraintValidatorContext) {

    // we are not validating on null.
    if (renewal == null || renewal.getRenewalDate() == null) {
      return true;
    }

    // Renewal year must be same year as renewal date or for the year early.  Covers late renewals.
    return renewal.getRenewalYear() == renewal.getRenewalDate().getYear()
        || renewal.getRenewalYear() == renewal.getRenewalDate().getYear() - 1;
  }
}
