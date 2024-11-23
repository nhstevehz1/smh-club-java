package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.models.EmailModel;

import java.util.List;

public interface EmailMapper {

    EmailEntity toEntity(EmailModel model);

    EmailModel toModel(EmailEntity entity);

    EmailEntity updateEntity(EmailModel model, EmailEntity entity);

    List<EmailModel> toModelList(List<EmailEntity> entityList);
}
