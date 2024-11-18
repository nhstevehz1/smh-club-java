package com.smh.club.api.data.mappers;

import com.smh.club.api.data.contracts.mappers.RenewalMapper;
import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.data.dto.RenewalDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenewalMapperImpl extends DomainDataMapper implements RenewalMapper {
    
    public RenewalMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public RenewalEntity toEntity(RenewalDto createDto) {
        return modelMapper.map(createDto, RenewalEntity.class);
    }

    @Override
    public RenewalDto toDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalDto.class);
    }

    @Override
    public RenewalEntity updateEntity(RenewalDto updateDto, RenewalEntity entity) {
        modelMapper.map(updateDto, entity);
        return entity;
    }

    @Override
    public List<RenewalDto> toDtoList(List<RenewalEntity> entityList) {
        return mapList(entityList, RenewalDto.class);
    }
}
