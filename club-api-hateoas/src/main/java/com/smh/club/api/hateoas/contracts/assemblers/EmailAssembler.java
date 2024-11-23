package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.models.EmailModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface EmailAssembler {

    EmailModel toModel(EmailEntity entity);

    PagedModel<EmailModel> toPagedModel(Page<EmailEntity> page);
}
