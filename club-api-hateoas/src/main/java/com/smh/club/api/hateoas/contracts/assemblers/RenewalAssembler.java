package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

/**
 * Adds hyper media links an {@link RenewalModel} that points to the resource it represents.
 */
public interface RenewalAssembler {

    /**
     * Converts a {@link RenewalEntity} to a {@link RenewalModel}
     * @param entity The {@link RenewalEntity} to convert.
     * @return An {@link RenewalModel} that contains links to itself.
     */
    RenewalModel toModel(RenewalEntity entity);

    /**
     * Converts a {@link Page} of type {@link RenewalEntity} to a {@link PagedModel}
     * of type {@link RenewalModel}
     * @param page The {@link Page} to be converted.
     * @return A {@link PagedModel} of type {@link RenewalModel}
     */
    PagedModel<RenewalModel> toPagedModel(Page<RenewalEntity> page);
}
