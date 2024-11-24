package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;
import com.smh.club.api.hateoas.models.PhoneModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

/**
 * Adds hyper media links an {@link PhoneModel} that points to the resource it represents.
 */
public interface PhoneAssembler {

    /**
     * Converts an {@link PhoneEntity} to an {@link PhoneModel}
     * @param entity The {@link PhoneEntity} to convert.
     * @return An {@link PhoneModel} that contains links to itself.
     */
    PhoneModel toModel(PhoneEntity entity);

    /**
     * Converts a {@link Page} of type {@link PhoneEntity} to a {@link PagedModel}
     * of type {@link PhoneModel}
     * @param page The {@link Page} to be converted.
     * @return A {@link PagedModel} of type {@link PhoneModel}
     */
    PagedModel<PhoneModel> toPagedModel(Page<PhoneEntity> page);
}
