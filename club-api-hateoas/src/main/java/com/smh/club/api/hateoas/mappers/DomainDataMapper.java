package com.smh.club.api.hateoas.mappers;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DomainDataMapper {
    protected final ModelMapper modelMapper;

    public DomainDataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    protected <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
