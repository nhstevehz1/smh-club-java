package smh.club.shared.api.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import smh.club.shared.api.validators.SortValidator;

@Constraint(validatedBy = SortValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortConstraint {
  Class<?> value();
  String message () default "Sort contains an invalid column name.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
