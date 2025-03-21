package com.smh.club.api.validation;

import static java.time.temporal.ChronoUnit.YEARS;

import com.smh.club.api.dto.member.MemberBaseDto;
import com.smh.club.api.validation.constraints.BirthDate;
import com.smh.club.api.validation.constraints.ValidMember;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberValidator implements ConstraintValidator<ValidMember, MemberBaseDto> {


  @Override
  public void initialize(ValidMember constraintAnnotation) {
    var message = constraintAnnotation.message();
    log.debug("ValidMember message: {}", message);
  }

  @Override
  public boolean isValid(MemberBaseDto memberDto, ConstraintValidatorContext constraintValidatorContext) {

    try {
      var field = memberDto.getClass().getSuperclass().getDeclaredField("birthDate");
      var anno = field.getAnnotationsByType(BirthDate.class);
      var minAge = anno[0].minAge();
      log.debug("Min age value set in BirthDate constraint is: {}", minAge );

      // covert to ZonedDataTime so we can perform comparisons
      var joinedDate = memberDto.getJoinedDate().atZone(ZoneId.systemDefault());
      var birthDate = memberDto.getBirthDate().atZone(ZoneId.systemDefault());
     return (memberDto.getJoinedDate().compareTo(Instant.now()) <= 0)
          && (memberDto.getJoinedDate().compareTo(memberDto.getBirthDate()) >= 0)
          && YEARS.between(birthDate, joinedDate) >= minAge;

    } catch (Exception ex) {
      log.error("Cannot perform the validation.  Returning false.", ex);
      return false;
    }
  }
}
