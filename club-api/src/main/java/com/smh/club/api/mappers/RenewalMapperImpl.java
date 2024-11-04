package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.RenewalDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenewalMapperImpl extends DataObjectMapperBase implements RenewalMapper {
    
    public RenewalMapperImpl(ModelMapper mapper) {
        super(mapper);
        configureMapper(mapper);
    }

    @Override
    public RenewalEntity toEntity(RenewalDto dataObject) {
        return modelMapper.map(dataObject, RenewalEntity.class);
    }

    @Override
    public RenewalDto toDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalDto.class);
    }

    @Override
    public RenewalEntity updateEntity(RenewalDto dataObject, RenewalEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<RenewalDto> toDtoList(List<RenewalEntity> entityList) {
        return mapList(entityList, RenewalDto.class);
    }

    @Override
    protected void configureMapper(ModelMapper mapper) {
        TypeMap<RenewalEntity, RenewalDto> dtoTypeMap
                = this.modelMapper.createTypeMap(RenewalEntity.class, RenewalDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), RenewalDto::setMemberId));

        TypeMap<RenewalDto, RenewalEntity> entTypeMap
                = this.modelMapper.createTypeMap(RenewalDto.class, RenewalEntity.class);
        entTypeMap.addMappings(m -> m.skip(RenewalEntity::setMember));
    }
}
