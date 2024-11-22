package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.models.MemberModel;

import java.util.List;

public interface AddressMapper {

    MemberEntity toEntity(AddressModel model);

    MemberModel toModel(AddressEntity entity);

    MemberEntity updateEntity(AddressModel model, AddressEntity entity);

    List<AddressModel> toModelList(List<AddressEntity> entityList);
}
