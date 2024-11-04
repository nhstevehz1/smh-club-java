package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.PhoneDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhoneMapperImpl extends DataObjectMapperBase implements PhoneMapper {
    public PhoneMapperImpl(ModelMapper mapper) {
        super(mapper);
        configureMapper(mapper);
    }

    @Override
    public PhoneEntity toEntity(PhoneDto dataObject) {
        return modelMapper.map(dataObject, PhoneEntity.class);
    }

    @Override
    public PhoneDto toDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneDto.class);
    }

    @Override
    public PhoneEntity updateEntity(PhoneDto dataObject, PhoneEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<PhoneDto> toDtoList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneDto.class);
    }

    @Override
    protected void configureMapper(ModelMapper mapper) {
        TypeMap<PhoneEntity, PhoneDto> dtoTypeMap
                = this.modelMapper.createTypeMap(PhoneEntity.class, PhoneDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), PhoneDto::setMemberId));

        TypeMap<PhoneDto, PhoneEntity> entTypeMap
                = this.modelMapper.createTypeMap(PhoneDto.class, PhoneEntity.class);
        entTypeMap.addMappings(m -> m.skip(PhoneEntity::setMember));
    }
}
