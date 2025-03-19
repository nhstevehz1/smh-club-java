package com.smh.club.api.validation;

import static java.time.temporal.ChronoUnit.YEARS;

import com.smh.club.api.validation.constraints.BirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Validates the given {@link LocalDate} is less than or equal to the current date minus
 * the value specified in the {@link BirthDate} annotation.
 */
public class BirthDateValidator implements ConstraintValidator<BirthDate, Instant> {

  private int age;

  /**
   * Initializes the 'age' member to the value set in the {@link BirthDate} annotation.
   *
   * @param birthdate An instance of the {@link BirthDate} annotation.
   */
  @Override
  public void initialize(BirthDate birthdate) {
    age = birthdate.minAge();
  }

  /**
   * Determines if the given {@link Instant} is less than or equal to the current date minus
   * the value specified in the {@link BirthDate} annotation.
   *
   * @param birthDate An instance of {@link Instant} that is decorated with the {@link BirthDate} annotation.
   * @param constraintValidatorContext An instance of {@link ConstraintValidatorContext}.
   * @return True if the date is valid otherwise false.
   */
  @Override
  public boolean isValid(Instant birthDate, ConstraintValidatorContext constraintValidatorContext) {
    if (birthDate == null) {
      return true;
    }

    var zonedBirthDate = birthDate.atZone(ZoneId.systemDefault());
    return YEARS.between(zonedBirthDate, ZonedDateTime.now()) >= age;
  }
}
