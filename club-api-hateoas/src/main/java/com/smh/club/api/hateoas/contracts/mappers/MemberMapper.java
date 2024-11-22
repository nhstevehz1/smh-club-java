package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.MemberModel;

import java.util.List;

public interface MemberMapper {
    MemberEntity toEntity(MemberModel model);
    MemberModel toModel(MemberEntity entity);
    MemberEntity updateEntity(MemberModel model, MemberEntity entity);
    List<MemberModel> toModelList(List<MemberEntity> entityList);
}
