package com.smh.club.data.contracts.mappers;

import com.smh.club.data.domain.entities.RenewalEntity;
import com.smh.club.data.dto.RenewalDto;

import java.util.List;

public interface RenewalMapper {
    RenewalEntity toEntity(RenewalDto createDto);
    RenewalDto toDto(RenewalEntity entity);
    RenewalEntity updateEntity(RenewalDto updateDto, RenewalEntity entity);
    List<RenewalDto> toDtoList(List<RenewalEntity> entityList);
}
