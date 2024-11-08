package com.smh.club.api.common.mappers;


import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.dto.update.UpdateAddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toEntity(CreateAddressDto createDto);
    AddressDto toDto(AddressEntity entity);
    AddressEntity updateEntity(UpdateAddressDto updateDto, AddressEntity entity);
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
