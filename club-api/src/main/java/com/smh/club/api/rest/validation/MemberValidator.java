package com.smh.club.api.rest.validation;

import static java.time.temporal.ChronoUnit.YEARS;

import com.smh.club.api.rest.dto.MemberDto;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import com.smh.club.api.shared.validators.constraints.BirthDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberValidator implements ConstraintValidator<ValidMember, MemberDto> {


  @Override
  public void initialize(ValidMember constraintAnnotation) {
    var message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(MemberDto memberDto, ConstraintValidatorContext constraintValidatorContext) {

    try {
      var field = memberDto.getClass().getDeclaredField("birthDate");
      var anno = field.getAnnotationsByType(BirthDate.class);

      var minAge = anno[0].minAge();
      log.debug("Min age value set in BirthDate constraint is: {}", minAge );

     return memberDto.getJoinedDate().toEpochDay() <= LocalDate.now().toEpochDay()
          && memberDto.getJoinedDate().toEpochDay() >= memberDto.getBirthDate().toEpochDay()
          && YEARS.between(memberDto.getBirthDate(), memberDto.getJoinedDate()) >= minAge;

    } catch (Exception ex) {
      log.debug("Cannot perform the validation.  Returning false.");
      return false;
    }
  }
}
