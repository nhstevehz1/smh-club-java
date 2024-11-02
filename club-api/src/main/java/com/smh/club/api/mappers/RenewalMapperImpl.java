package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.data.dto.RenewalDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RenewalMapperImpl implements RenewalMapper {
    @Override
    public RenewalEntity toEntity(RenewalDto dataObject) {
        return RenewalEntity.builder()
                .renewalDate(dataObject.getRenewalDate())
                .renewalYear(dataObject.getRenewalYear())
                .build();
    }

    @Override
    public RenewalDto toDataObject(RenewalEntity entity) {
        return RenewalDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .renewalDate(entity.getRenewalDate())
                .renewalYear(entity.getRenewalYear())
                .build();
    }

    @Override
    public RenewalEntity updateEntity(RenewalDto dataObject, RenewalEntity entity) {
        entity.setRenewalDate(dataObject.getRenewalDate());
        entity.setRenewalYear((dataObject.getRenewalYear()));
        return entity;
    }

    @Override
    public List<RenewalDto> toDataObjectList(List<RenewalEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
