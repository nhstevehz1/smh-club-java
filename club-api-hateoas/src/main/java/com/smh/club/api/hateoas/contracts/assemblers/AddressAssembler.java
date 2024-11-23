package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface AddressAssembler {

    AddressModel toModel(AddressEntity entity);

    PagedModel<AddressModel> toPagedModel(Page<AddressEntity> page);
}
