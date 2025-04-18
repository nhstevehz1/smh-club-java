package com.smh.club.api.mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

/**
 * Base class for model mappers.
 */
public abstract class DomainDataMapper {

    protected final ModelMapper modelMapper;

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper}.
     */
    public DomainDataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Generic method for converting a {@link List} of one type to another type.
     */
    protected <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
            .stream()
            .map(element -> modelMapper.map(element, targetClass))
            .collect(Collectors.toList());
    }
}
