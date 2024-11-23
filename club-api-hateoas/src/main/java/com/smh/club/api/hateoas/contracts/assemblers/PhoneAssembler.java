package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface PhoneAssembler {

    PhoneModel toModel(PhoneEntity entity);

    PagedModel<PhoneModel> toPagedModel(Page<PhoneEntity> page);
}
