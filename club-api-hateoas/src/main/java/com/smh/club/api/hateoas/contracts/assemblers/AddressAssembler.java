package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.hateoas.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

/**
 * Adds hyper media links an {@link AddressModel} that points to the resource it represents.
 */
public interface AddressAssembler {

    /**
     * Converts an {@link AddressEntity} to an {@link AddressModel}
     * @param entity The {@link AddressEntity} to convert.
     * @return An {@link AddressModel} that contains links to itself.
     */
    AddressModel toModel(AddressEntity entity);

    /**
     * Converts a {@link Page} of type {@link AddressEntity} to a {@link PagedModel}
     * of type {@link AddressModel}
     * @param page The {@link Page} to be converted.
     * @return A {@link PagedModel} of type {@link AddressModel}
     */
    PagedModel<AddressModel> toPagedModel(Page<AddressEntity> page);
}
