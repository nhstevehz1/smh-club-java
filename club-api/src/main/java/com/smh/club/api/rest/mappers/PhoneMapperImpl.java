package com.smh.club.api.rest.mappers;

import com.smh.club.api.data.contracts.mappers.PhoneMapper;
import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.dto.PhoneDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    public PhoneMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public PhoneEntity toEntity(PhoneDto createPhoneDto) {
        return modelMapper.map(createPhoneDto, PhoneEntity.class);
    }

    @Override
    public PhoneDto toDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneDto.class);
    }

    public PhoneEntity updateEntity(PhoneDto updatePhoneDto, PhoneEntity entity) {
        modelMapper.map(updatePhoneDto, entity);
        return entity;
    }

    @Override
    public List<PhoneDto> toDtoList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneDto.class);
    }
}
