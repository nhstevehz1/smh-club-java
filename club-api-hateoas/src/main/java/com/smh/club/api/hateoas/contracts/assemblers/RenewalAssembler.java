package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface RenewalAssembler {

    RenewalModel toModel(RenewalEntity entity);

    PagedModel<RenewalModel> toPagedModel(Page<RenewalEntity> page);
}
