package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.MemberModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface AddressAssembler {

    MemberModel toModel(AddressEntity entity);

    PagedModel<MemberModel> toPagedModel(Page<AddressEntity> page);
}
