package com.smh.club.api.hateoas.mappers;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

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
     * Generic method for converting a {@link List} of one to type to another type.
     */
    protected <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
            .stream()
            .map(element -> modelMapper.map(element, targetClass))
            .collect(Collectors.toList());
    }
}
