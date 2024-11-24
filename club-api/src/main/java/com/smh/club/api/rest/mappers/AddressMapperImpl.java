package com.smh.club.api.rest.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.rest.contracts.mappers.AddressMapper;
import com.smh.club.api.rest.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import smh.club.shared.mappers.DomainDataMapper;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Component
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper  {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public AddressMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity toEntity(AddressDto dto) {
        return modelMapper.map(dto, AddressEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressDto toDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity updateEntity(AddressDto dto, AddressEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AddressDto> toDtoList(List<AddressEntity> source) {
        return mapList(source, AddressDto.class);
    }
}
