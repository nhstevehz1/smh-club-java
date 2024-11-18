package com.smh.club.data.contracts.mappers;

import com.smh.club.data.domain.entities.AddressEntity;
import com.smh.club.data.dto.AddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toEntity(AddressDto createDto);
    AddressDto toDto(AddressEntity entity);
    AddressEntity updateEntity(AddressDto updateDto, AddressEntity entity);
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
