package com.smh.club.api.data.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.dto.AddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toEntity(AddressDto createDto);
    AddressDto toDto(AddressEntity entity);
    AddressEntity updateEntity(AddressDto updateDto, AddressEntity entity);
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
