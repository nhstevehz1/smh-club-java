package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapperImpl extends DataObjectMapperBase implements AddressMapper  {

    public AddressMapperImpl(ModelMapper mapper) {
        super(mapper);
        configureMapper(mapper);
    }

    @Override
    public AddressEntity toEntity(AddressCreateDto createDto) {
        return modelMapper.map(createDto, AddressEntity.class);
    }

    @Override
    public AddressDto toDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressDto.class);
    }

    @Override
    public AddressEntity updateEntity(AddressCreateDto dataObject, AddressEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<AddressDto> toDtoList(List<AddressEntity> source) {
        return mapList(source, AddressDto.class);
    }

    @Override
    protected void configureMapper(ModelMapper mapper) {
        TypeMap<AddressEntity, AddressDto> dtoTypeMap
                = this.modelMapper.createTypeMap(AddressEntity.class, AddressDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), AddressDto::setMemberId));


        TypeMap<AddressDto, AddressEntity> entTypeMap
                = this.modelMapper.createTypeMap(AddressDto.class, AddressEntity.class);
        entTypeMap.addMappings(m -> m.skip(AddressEntity::setMember));
    }
}
