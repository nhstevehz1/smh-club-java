package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.CreateAddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper  {

    public AddressMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public AddressEntity toEntity(CreateAddressDto createDto) {
        return modelMapper.map(createDto, AddressEntity.class);
    }

    @Override
    public AddressDto toDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressDto.class);
    }

    @Override
    public AddressEntity updateEntity(CreateAddressDto dataObject, AddressEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<AddressDto> toDtoList(List<AddressEntity> source) {
        return mapList(source, AddressDto.class);
    }
}
