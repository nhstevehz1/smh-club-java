package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.data.entities.PhoneEntity;
import com.smh.club.api.data.dto.PhoneDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneMapperImpl implements PhoneMapper {
    @Override
    public PhoneEntity toEntity(PhoneDto dataObject) {
        return PhoneEntity.builder()
                .phoneNum(dataObject.getPhoneNum())
                .phoneType(dataObject.getPhoneType())
                .build();
    }

    @Override
    public PhoneDto toDataObject(PhoneEntity entity) {
        return PhoneDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .phoneNum(entity.getPhoneNum())
                .phoneType(entity.getPhoneType())
                .build();
    }

    @Override
    public PhoneEntity updateEntity(PhoneDto dataObject, PhoneEntity entity) {
        entity.setPhoneNum(dataObject.getPhoneNum());
        entity.setPhoneType(dataObject.getPhoneType());
        return entity;
    }

    @Override
    public List<PhoneDto> toDataObjectList(List<PhoneEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
