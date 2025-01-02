package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.MemberModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

/**
 * Adds hyper media links a {@link MemberModel} that points to the resource it represents.
 */
public interface MemberAssembler {

    /**
     * Converts a {@link MemberEntity} to a {@link MemberModel}
     * @param entity The {@link MemberEntity} to convert.
     * @return An {@link MemberModel} that contains links to itself.
     */
    MemberModel toModel(MemberEntity entity);

    /**
     * Converts a {@link Page} of type {@link MemberEntity} to a {@link PagedModel}
     * of type {@link MemberModel}
     * @param page The {@link Page} to be converted.
     * @return A {@link PagedModel} of type {@link MemberModel}
     */
    PagedModel<MemberModel> toPagedModel(Page<MemberEntity> page);
}
