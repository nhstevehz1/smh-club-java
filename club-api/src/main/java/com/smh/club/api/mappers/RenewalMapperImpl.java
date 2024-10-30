package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.models.Renewal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RenewalMapperImpl implements RenewalMapper {
    @Override
    public RenewalEntity toEntity(Renewal dataObject) {
        return RenewalEntity.builder()
                .renewalDate(dataObject.getRenewalDate())
                .renewalYear(dataObject.getRenewalYear())
                .build();
    }

    @Override
    public Renewal toDataObject(RenewalEntity entity) {
        return Renewal.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .renewalDate(entity.getRenewalDate())
                .renewalYear(entity.getRenewalYear())
                .build();
    }

    @Override
    public void updateEntity(Renewal dataObject, RenewalEntity entity) {
        entity.setRenewalDate(dataObject.getRenewalDate());
        entity.setRenewalYear((dataObject.getRenewalYear()));
    }

    @Override
    public List<Renewal> toDataObjectList(List<RenewalEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
