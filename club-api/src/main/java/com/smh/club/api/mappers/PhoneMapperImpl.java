package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.data.entities.PhoneEntity;
import com.smh.club.api.models.Phone;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneMapperImpl implements PhoneMapper {
    @Override
    public PhoneEntity toEntity(Phone dataObject) {
        return PhoneEntity.builder()
                .phoneNum(dataObject.getPhoneNum())
                .phoneType(dataObject.getPhoneType())
                .build();
    }

    @Override
    public Phone toDataObject(PhoneEntity entity) {
        return Phone.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .phoneNum(entity.getPhoneNum())
                .phoneType(entity.getPhoneType())
                .build();
    }

    @Override
    public void update(Phone dataObject, PhoneEntity entity) {
        entity.setPhoneNum(dataObject.getPhoneNum());
        entity.setPhoneType(dataObject.getPhoneType());
    }

    @Override
    public List<Phone> toDataObjectList(List<PhoneEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
