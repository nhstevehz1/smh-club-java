package com.smh.club.api.hateoas.validation;

import static java.time.temporal.ChronoUnit.YEARS;

import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.validation.constraints.ValidMember;
import com.smh.club.api.shared.validators.constraints.BirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberValidator implements ConstraintValidator<ValidMember, MemberModel> {


  public boolean isValid(MemberModel member, ConstraintValidatorContext constraintValidatorContext) {

    try {
      var field = member.getClass().getDeclaredField("birthDate");
      var anno = field.getAnnotationsByType(BirthDate.class);

      var minAge = anno[0].minAge();
      log.debug("Min age value set in BirthDate constraint is: {}", minAge );

     return member.getJoinedDate().toEpochDay() <= LocalDate.now().toEpochDay()
          && member.getJoinedDate().toEpochDay() >= member.getBirthDate().toEpochDay()
          && YEARS.between(member.getBirthDate(), member.getJoinedDate()) >= minAge;

    } catch (Exception ex) {
      log.debug("Cannot perform the validation.  Returning false.");
      return false;
    }
  }
}
