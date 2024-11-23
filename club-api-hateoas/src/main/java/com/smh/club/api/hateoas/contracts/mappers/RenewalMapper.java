package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;

import java.util.List;

public interface RenewalMapper {

    RenewalEntity toEntity(RenewalModel model);

    RenewalModel toModel(RenewalEntity entity);

    RenewalEntity updateEntity(RenewalModel model, RenewalEntity entity);

    List<RenewalModel> toModelList(List<RenewalEntity> entityList);
}
