package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.CreateRenewalDto;
import com.smh.club.api.dto.RenewalDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RenewalMapperImpl extends DomainDataMapper implements RenewalMapper {
    
    public RenewalMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public RenewalEntity toEntity(CreateRenewalDto createDto) {
        return modelMapper.map(createDto, RenewalEntity.class);
    }

    @Override
    public RenewalDto toDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalDto.class);
    }

    @Override
    public RenewalEntity updateEntity(CreateRenewalDto updateDto, RenewalEntity entity) {
        modelMapper.map(updateDto, entity);
        return entity;
    }

    @Override
    public List<RenewalDto> toDtoList(List<RenewalEntity> entityList) {
        return mapList(entityList, RenewalDto.class);
    }
}
