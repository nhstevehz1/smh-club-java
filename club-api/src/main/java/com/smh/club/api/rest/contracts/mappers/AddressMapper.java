package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.rest.dto.AddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toEntity(AddressDto createDto);
    AddressDto toDto(AddressEntity entity);
    AddressEntity updateEntity(AddressDto updateDto, AddressEntity entity);
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
