package com.smh.club.data.mappers;

import com.smh.club.data.contracts.mappers.AddressMapper;
import com.smh.club.data.domain.entities.AddressEntity;
import com.smh.club.data.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper  {

    public AddressMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public AddressEntity toEntity(AddressDto createDto) {
        return modelMapper.map(createDto, AddressEntity.class);
    }

    @Override
    public AddressDto toDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressDto.class);
    }

    @Override
    public AddressEntity updateEntity(AddressDto dataObject, AddressEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<AddressDto> toDtoList(List<AddressEntity> source) {
        return mapList(source, AddressDto.class);
    }
}
