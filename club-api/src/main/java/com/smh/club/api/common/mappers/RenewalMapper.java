package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;

import java.util.List;

public interface RenewalMapper {
    RenewalEntity toEntity(CreateRenewalDto createDto);

    RenewalDto toDto(RenewalEntity entity);

    RenewalEntity updateEntity(UpdateRenewalDto updateDto, RenewalEntity entity);

    List<RenewalDto> toDtoList(List<RenewalEntity> entityList);
}
