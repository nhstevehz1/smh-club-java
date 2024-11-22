package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;

import java.util.List;

public interface AddressMapper {

    AddressEntity toEntity(AddressModel model);

    AddressModel toModel(AddressEntity entity);

    AddressEntity updateEntity(AddressModel model, AddressEntity entity);

    List<AddressModel> toModelList(List<AddressEntity> entityList);
}
