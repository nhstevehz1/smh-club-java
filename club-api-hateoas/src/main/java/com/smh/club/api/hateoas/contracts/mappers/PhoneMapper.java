package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;

import java.util.List;

public interface PhoneMapper {
    
    PhoneEntity toEntity(PhoneModel model);

    PhoneModel toModel(PhoneEntity entity);

    PhoneEntity updateEntity(PhoneModel model, PhoneEntity entity);

    List<PhoneModel> toModelList(List<PhoneEntity> entityList);
}
