package com.smh.club.api.validation;

import com.smh.club.api.dto.member.MemberDto;
import com.smh.club.api.validation.constraints.BirthDate;
import com.smh.club.api.validation.constraints.ValidMember;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;

@Slf4j
public class MemberValidator implements ConstraintValidator<ValidMember, MemberDto> {


  @Override
  public void initialize(ValidMember constraintAnnotation) {
    var message = constraintAnnotation.message();
    log.debug("ValidMember message: {}", message);
  }

  @Override
  public boolean isValid(MemberDto memberDto, ConstraintValidatorContext constraintValidatorContext) {

    try {
      var fields = getAllFields(memberDto.getClass());
      var field = fields.stream()
          .filter(f -> f.getName().equals("birthDate"))
          .findFirst()
          .orElseThrow();

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

  private List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields;
    fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

    var superClass = clazz.getSuperclass();
    if(superClass != null) {
      fields.addAll(getAllFields(superClass));
    }

    return fields;
  }
}
