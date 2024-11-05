package com.smh.club.api.common.mappers;


import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;

import java.util.List;

public interface AddressMapper {
    AddressEntity toAddressEntity(AddressCreateDto addressCreateDto);
    AddressDto toAddressDto(AddressEntity addressEntity);
    AddressEntity updateAddressEntity(AddressCreateDto addressCreateDto, AddressEntity entity);
    List<AddressDto> toAddressDtoList(List<AddressEntity> addressEntityList);
}
