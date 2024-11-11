package com.smh.club.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortDefault;
import com.smh.club.api.annotations.SortExclude;
import com.smh.club.api.annotations.SortTarget;
import lombok.Builder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractServiceBase {

    protected abstract String getSortColumn(String key);

    protected <S,T> Optional<String> getSort(String key, Class<S> source, Class<T> target){
        var sourceField = getSourceField(key, source);
        return sourceField.flatMap(field -> getSortFieldName(target, field));
    }

    protected <S,T> Optional<String> getDefaultSort(Class<S> source, Class<T> target) {
        var sourceField = getDefaultSourceField(source);
        return sourceField.flatMap(field -> getSortFieldName(target, field));
    }

    private <S> Optional<SourceField> getDefaultSourceField(Class<S> source) {
        return Arrays.stream(source.getDeclaredFields())
                .filter(f -> isNotExcluded(f) &&  isDefault(f))
                .map(f -> SourceField.builder()
                        .field(f.getName())
                        .target(getTargetPropValue(f))
                        .build())
                .findFirst();
    }

    private <S>  Optional<SourceField> getSourceField(String key, Class<S> source) {
        return Arrays.stream(source.getDeclaredFields())
                .filter(f -> isNotExcluded(f) &&
                        (Objects.equals(getJsonPropValue(f), key) || f.getName().equals(key)))
                .map(f -> SourceField.builder()
                        .field(f.getName())
                        .target(getTargetPropValue(f))
                        .build())
                .findFirst();
    }

    private <T> Optional<String> getSortFieldName(Class<T> target, SourceField sf) {
        return Arrays.stream(target.getDeclaredFields())
                .filter(f -> isNotExcluded(f) &&
                        (f.getName().equals(sf.target)) || f.getName().equals(sf.field))
                .map(Field::getName)
                .findFirst();
    }

    private boolean isDefault(Field field) {
        return field.getAnnotation(SortDefault.class) != null;
    }

    private boolean isNotExcluded(Field field) {
        return field.getAnnotation(SortExclude.class) == null;
    }

    private String getJsonPropValue(Field field) {
        var jsonProp = field.getAnnotation(JsonProperty.class);
        return jsonProp != null ? jsonProp.value() : null;
    }

    private String getTargetPropValue(Field field) {
        var sortTargetProp = field.getAnnotation(SortTarget.class);
        return sortTargetProp != null ? sortTargetProp.value() : null;
    }

    @Builder
    private record SourceField(String field, String target, boolean isDefault) {}
}
