package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.rest.dto.PhoneDto;

import java.util.List;

public interface PhoneMapper {
    PhoneEntity toEntity(PhoneDto createPhoneDto);

    PhoneDto toDto(PhoneEntity entity);

    List<PhoneDto> toDtoList(List<PhoneEntity> entityList);

    PhoneEntity updateEntity(PhoneDto updatePhoneDto, PhoneEntity entity);
}
