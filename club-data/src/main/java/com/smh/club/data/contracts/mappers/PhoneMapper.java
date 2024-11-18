package com.smh.club.data.contracts.mappers;

import com.smh.club.data.domain.entities.PhoneEntity;
import com.smh.club.data.dto.PhoneDto;

import java.util.List;

public interface PhoneMapper {
    PhoneEntity toEntity(PhoneDto createPhoneDto);

    PhoneDto toDto(PhoneEntity entity);

    List<PhoneDto> toDtoList(List<PhoneEntity> entityList);

    PhoneEntity updateEntity(PhoneDto updatePhoneDto, PhoneEntity entity);
}
