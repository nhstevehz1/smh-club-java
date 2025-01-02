package com.smh.club.api.hateoas.contracts.assemblers;

import com.smh.club.api.hateoas.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.models.EmailModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

/**
 * Adds hyper media links an {@link EmailModel} that points to the resource it represents.
 */
public interface EmailAssembler {

    /**
     * Converts an {@link EmailEntity} to an {@link EmailModel}
     * @param entity The {@link EmailEntity} to convert.
     * @return An {@link EmailModel} that contains links to itself.
     */
    EmailModel toModel(EmailEntity entity);

    /**
     * Converts a {@link Page} of type {@link EmailEntity} to a {@link PagedModel}
     * of type {@link EmailModel}
     * @param page The {@link Page} to be converted.
     * @return A {@link PagedModel} of type {@link EmailModel}
     */
    PagedModel<EmailModel> toPagedModel(Page<EmailEntity> page);
}
