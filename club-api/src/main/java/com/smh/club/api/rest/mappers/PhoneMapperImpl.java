package com.smh.club.api.rest.mappers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.rest.contracts.mappers.PhoneMapper;
import com.smh.club.api.rest.dto.PhoneDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import smh.club.shared.mappers.DomainDataMapper;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Component
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public PhoneMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity toEntity(PhoneDto createPhoneDto) {
        return modelMapper.map(createPhoneDto, PhoneEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneDto toDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity updateEntity(PhoneDto dto, PhoneEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhoneDto> toDtoList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneDto.class);
    }
}
