package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.rest.dto.RenewalDto;

import java.util.List;

public interface RenewalMapper {
    RenewalEntity toEntity(RenewalDto createDto);
    RenewalDto toDto(RenewalEntity entity);
    RenewalEntity updateEntity(RenewalDto updateDto, RenewalEntity entity);
    List<RenewalDto> toDtoList(List<RenewalEntity> entityList);
}
