package com.smh.club.api.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortAlias;
import com.smh.club.api.annotations.SortExclude;
import com.smh.club.api.annotations.SortTarget;
import com.smh.club.api.validation.constraints.SortConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

/**
 *
 * Validates the given Pageable.Sort parameter specifies a valid sort column name.
 *
 */
public class SortValidator implements ConstraintValidator<SortConstraint, Pageable> {

  private List<String> allowedSortNames;

  /**
   * <p>
   * Get a list of allowed sort fields
   * </p>
   * <p>
   * if a field contains an @SortTarget,
   *   then use the underlying field name if it exists in the entity name list.
   * </p>
   * <p>
   * if a field has an @JsonProperty
   *   then use the @JsonProperty value if underlying field name exists in the entity name list
   * </p>
   * <p>
   * if a field has both an @SortTarget and an @JsonProperty
   *   then use the @JsonProperty value if the @SortTarget value exists in the entity name list.
   * </p>
   * <p>
   * if a field has both an @SortAlias
   *   then include the @SortAlias value, the @JsonProperty value (if one exists), and the underlying field name
   *   in the allowed list.
   *  </p>
   *
   *
   * @param constraintAnnotation An instance of the {@link SortConstraint} annotation.
   */
  @Override
  public void initialize(SortConstraint constraintAnnotation) {

    // Get the matching entity type
    var sortEntity = constraintAnnotation.value().getAnnotation(SortTarget.class);

    // Get a list of fields from the entity class
    var entityFields = Arrays.stream(sortEntity.value().getDeclaredFields())
        .map(Field::getName).toList();

    // Get a list of fields from the dto and its super classes
    var fields = getAllFields(constraintAnnotation.value());

    var dtoFields = fields.stream().map(DtoSortField::of).toList();

    var sortAliases =
        dtoFields
            .stream()
            .map(DtoSortField::getSortAliasName)
            .filter(Objects::nonNull).toList();

    // Gather a list of allowed fields based on the criteria described above
    var mappedNames =
        dtoFields
          .stream()
          .filter(sf ->
              // filter not excluded and either the entity contains the filed or a sort alias exists
              sf.isNotExcluded() && (entityFields.contains(sf.getDtoFieldName())) || sf.sortAliasName != null)
          .map(sf -> sf.getJsonPropName() !=null ? sf.getJsonPropName() : sf.getDtoFieldName())
          .toList();

    // Gather a list of the underlining field names
    var dtoNames =
        dtoFields
            .stream()
            .filter(DtoSortField::isNotExcluded)
            .map(DtoSortField::getDtoFieldName).toList();

    // concatenate the two lists while removing duplicates
    var mappedAndDto = Stream.concat(mappedNames.stream(), dtoNames.stream()).distinct().toList();
    allowedSortNames = Stream.concat(mappedAndDto.stream(), sortAliases.stream()).toList();

  }

  /**
   * Determines if the given Pageable contains valid sort column(s).
   *
   * @param pageable An instance of {@link Pageable} that is decorated with the {@link SortConstraint} annotation.
   * @param constraintValidatorContext An instance of {@link ConstraintValidatorContext}.
   * @return True if the pageable is unsorted or contains valid sort column(s).  Otherwise, false.
   */
  @Override
  public boolean isValid(Pageable pageable, ConstraintValidatorContext constraintValidatorContext) {
    // Allow unsorted.
    if ( pageable.getSort().isUnsorted()) {
      return true;
    }

    // return false if any of the sorts are not in the allowed list.
    return  !pageable.getSort().get()
        .map(o -> allowedSortNames.contains(o.getProperty()))
        .toList()
        .contains(false);
  }

  private List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields;
    fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

    Class<?> superClass = clazz.getSuperclass();

    // recurse on super classes
    if (superClass != null) {
      fields.addAll(getAllFields(superClass));
    }

    return fields;
  }

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static class DtoSortField {
    private final String jsonPropName;
    private final String dtoFieldName;
    private final String sortAliasName;
    private final boolean isNotExcluded;

    public static DtoSortField of(Field field) {

      var notExcluded = field.getAnnotation(SortExclude.class) == null;

      var sortAliasProp = field.getAnnotation(SortAlias.class);
      var sortAliasValue = sortAliasProp != null ? sortAliasProp.value() : null;

      var jsonProp = field.getAnnotation(JsonProperty.class);
      var jsonPropValue = jsonProp != null ? jsonProp.value() : null;

      var fieldName = field.getName();

      return new DtoSortField(jsonPropValue, fieldName, sortAliasValue, notExcluded );

    }
  }
}
