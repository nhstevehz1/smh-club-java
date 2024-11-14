package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    public PhoneMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public PhoneEntity toEntity(CreatePhoneDto createPhoneDto) {
        return modelMapper.map(createPhoneDto, PhoneEntity.class);
    }

    @Override
    public PhoneDto toDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneDto.class);
    }

    public PhoneEntity updateEntity(CreatePhoneDto updatePhoneDto, PhoneEntity entity) {
        modelMapper.map(updatePhoneDto, entity);
        return entity;
    }

    @Override
    public List<PhoneDto> toDtoList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneDto.class);
    }
}
