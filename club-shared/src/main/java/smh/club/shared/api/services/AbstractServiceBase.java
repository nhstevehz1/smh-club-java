package smh.club.shared.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import smh.club.shared.api.annotations.SortExclude;

/**
 * And abstract service base class.
 */
public abstract class AbstractServiceBase {

    protected String getSortColumn(String key) {return null;}

    /**
     * Maps the Sort.Order's in to actual fields
     *
     * @param sort The input {@link Sort}.
     * @return The mapped {@link Sort}
     */
    protected Sort getSort(Sort sort) {return null;} // TODO: make abstract after refactor

    /**
     * Retrieves an entity's column name based on a key.
     *
     * @param source The model class.
     * @param target The entity class.
     * @return An {@link Optional} {@link String} representing the sort column name.
     */
    protected <S, T> Optional<String> getSort(String key, Class<S> source, Class<T> target) {
        var sourceField = getSourceField(key, source);
        return sourceField.flatMap(field -> getSortFieldName(target, field));
    }

    private <S> Optional<String> getSourceField(String key, Class<S> source) {
        return Arrays.stream(source.getDeclaredFields())
            .filter(f -> isNotExcluded(f) &&
                (Objects.equals(getJsonPropValue(f), key) || f.getName().equals(key)))
            .map(Field::getName)
            .findFirst();
    }

    private <T> Optional<String> getSortFieldName(Class<T> target, String sortField) {
        return Arrays.stream(target.getDeclaredFields())
            .filter(f -> isNotExcluded(f) && f.getName().equals(sortField))
            .map(Field::getName)
            .findFirst();
    }

    private boolean isNotExcluded(Field field) {
        return field.getAnnotation(SortExclude.class) == null;
    }

    private String getJsonPropValue(Field field) {
        var jsonProp = field.getAnnotation(JsonProperty.class);
        return jsonProp != null ? jsonProp.value() : null;
    }
}
