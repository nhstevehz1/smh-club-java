package com.smh.club.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.annotations.SortAlias;
import com.smh.club.api.rest.annotations.SortExclude;
import java.lang.reflect.Field;
import java.util.*;
import org.springframework.data.domain.Sort;

/**
 * And abstract service base class.
 */
public abstract class AbstractServiceBase {

    /**
     * Maps the Sort.Order's in to actual fields
     *
     * @param sort The input {@link Sort}.
     * @return The mapped {@link Sort}
     */
    protected abstract Sort getSort(Sort sort);

    /**
     * Retrieves an entity's column name based on a key.
     *
     * @param source The model class.
     * @param target The entity class.
     * @return An {@link Optional} {@link String} representing the sort column name.
     */
    protected <S, T> Optional<String> getSort(String key, Class<S> source, Class<T> target) {
        var sourceField = getSortFieldData(key, source);
        return sourceField.flatMap(field ->
            field.sortAlias == null
                ? getSortFieldName(target, field.fieldName)
                : Optional.of(field.sortAlias));
    }

    private <S> Optional<SortFieldData> getSortFieldData(String key, Class<S> source) {
        return getAllFields(source).stream()
            .filter(f -> isNotExcluded(f) &&
                (Objects.equals(getJsonPropValue(f), key)
                    || f.getName().equals(key))
                    || Objects.equals(getSortAliasValue(f), key))
            .map(f -> {
              var alias = getSortAliasValue(f);
              var jsonProp = getJsonPropValue(f);
              return new SortFieldData(alias, jsonProp, f.getName());
            })
            .findFirst();
    }

    private <T> Optional<String> getSortFieldName(Class<T> target, String sortField) {
        return getAllFields(target).stream()
            .filter(f -> isNotExcluded(f) && f.getName().equals(sortField))
            .map(Field::getName)
            .findFirst();
    }

    // Supports only one level of inheritance.
    private <T> List<Field> getAllFields(Class<T> clazz) {
        List<Field> fields =
            new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> superClass = clazz.getSuperclass();

        if (superClass != null) {

          fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }

        return fields;
    }

    private boolean isNotExcluded(Field field) {
        return field.getAnnotation(SortExclude.class) == null;
    }

    private String getJsonPropValue(Field field) {
        var jsonProp = field.getAnnotation(JsonProperty.class);
        return jsonProp != null ? jsonProp.value() : null;
    }

    private String getSortAliasValue(Field field) {
        var aliasProp = field.getAnnotation(SortAlias.class);
        return aliasProp != null ? aliasProp.value() : null;
    }

    private record SortFieldData(
        String sortAlias,
        String jsonProp,
        String fieldName) { }
}
