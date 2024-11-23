package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.MemberModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface MemberAssembler {
    MemberModel toModel(MemberEntity entity);

    PagedModel<MemberModel> toPagedModel(Page<MemberEntity> page);
}
