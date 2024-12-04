package com.smh.club.api.shared.validators;

import static java.time.temporal.ChronoUnit.YEARS;

import com.smh.club.api.shared.validators.constraints.BirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Validates the given {@link LocalDate} is less than or equal to the current date minus
 * the value specified in the {@link BirthDate} annotation.
 */
public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {

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
   * Determines if the given {@link LocalDate} is less than or equal to the current date minus
   * the value specified in the {@link BirthDate} annotation.
   *
   * @param birthDate An instance of {@link LocalDate} that is decorated with the {@link BirthDate} annotation.
   * @param constraintValidatorContext An instance of {@link ConstraintValidatorContext}.
   * @return True if the date is valid otherwise false.
   */
  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
    return birthDate == null || YEARS.between(birthDate, LocalDate.now()) >= age;
  }
}
