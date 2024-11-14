package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.PhoneDto;

import java.util.List;

public interface PhoneMapper {
    PhoneEntity toEntity(PhoneDto createPhoneDto);

    PhoneDto toDto(PhoneEntity entity);

    List<PhoneDto> toDtoList(List<PhoneEntity> entityList);

    PhoneEntity updateEntity(PhoneDto updatePhoneDto, PhoneEntity entity);
}
