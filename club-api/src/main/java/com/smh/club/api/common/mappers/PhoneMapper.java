package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;

import java.util.List;

public interface PhoneMapper {
    PhoneEntity toEntity(CreatePhoneDto createPhoneDto);

    PhoneDto toDto(PhoneEntity entity);

    List<PhoneDto> toDtoList(List<PhoneEntity> entityList);

    PhoneEntity updateEntity(UpdatePhoneDto updatePhoneDto, PhoneEntity entity);
}
