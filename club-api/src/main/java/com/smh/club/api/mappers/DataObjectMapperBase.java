package com.smh.club.api.mappers;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataObjectMapperBase {

    protected final ModelMapper modelMapper;

    public DataObjectMapperBase(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    protected abstract void configureMapper(ModelMapper mapper);

    protected <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
