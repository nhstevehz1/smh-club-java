package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.hateoas.contracts.assemblers.EmailAssembler;
import com.smh.club.api.hateoas.contracts.mappers.EmailMapper;
import com.smh.club.api.hateoas.controllers.EmailController;
import com.smh.club.api.hateoas.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.models.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * {@inheritDoc}
 * Extends {@link RepresentationModelAssemblerSupport}
 */
@Component
public class EmailAssemblerImpl
    extends RepresentationModelAssemblerSupport<EmailEntity, EmailModel>
    implements EmailAssembler {

    private final EmailMapper mapper;
    private final PagedResourcesAssembler<EmailEntity> pagedAssembler;

    /**
     * Constructor.
     * @param mapper An {@link EmailMapper} used to copy the properties to an {@link EmailModel}
     *               from an {@link EmailEntity}.
     * @param pagedAssembler A {@link PagedResourcesAssembler} of type {@link EmailEntity} used to
     *                       copy a {@link Page} to a {@link PagedModel}.
     * @see EmailAssemblerImpl#toPagedModel
     */
    @Autowired
    public EmailAssemblerImpl(EmailMapper mapper, PagedResourcesAssembler<EmailEntity> pagedAssembler) {
        super(EmailController.class, EmailModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public EmailModel toModel(@NonNull EmailEntity entity) {
        EmailModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(EmailController.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(EmailController.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<EmailModel> toPagedModel(Page<EmailEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    /**
     * Called internally.  Responsible for mapping an {@link EmailEntity} to a {@link EmailModel}.
     * Override the default behavior of using reflection
     * @param entity The {@link EmailEntity} to be mapped
     * @return The mapped {@link EmailModel}l
     */
    @NonNull
    @Override
    protected EmailModel instantiateModel(@NonNull EmailEntity entity) {
        return mapper.toModel(entity);
    }
}
