package com.smh.club.api.common.mappers;


import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toEntity(AddressCreateDto createDto);
    AddressDto toDto(AddressEntity entity);
    AddressEntity updateEntity(AddressCreateDto updateDto, AddressEntity entity);
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
