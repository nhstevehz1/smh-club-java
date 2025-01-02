package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.hateoas.contracts.assemblers.RenewalAssembler;
import com.smh.club.api.hateoas.contracts.mappers.RenewalMapper;
import com.smh.club.api.hateoas.controllers.RenewalController;
import com.smh.club.api.hateoas.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;
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
public class RenewalAssemblerImpl extends RepresentationModelAssemblerSupport<RenewalEntity, RenewalModel>
    implements RenewalAssembler {

    private final RenewalMapper mapper;
    private final PagedResourcesAssembler<RenewalEntity> pagedAssembler;

    /**
     * Constructor.
     * @param mapper An {@link RenewalMapper} used to copy the properties to a {@link RenewalModel}
     *               from a {@link RenewalEntity}.
     * @param pagedAssembler A {@link PagedResourcesAssembler} of type {@link RenewalEntity} used to
     *                       copy a {@link Page} to a {@link PagedModel}.
     * @see RenewalAssemblerImpl#toPagedModel
     */
    @Autowired
    public RenewalAssemblerImpl(RenewalMapper mapper, PagedResourcesAssembler<RenewalEntity> pagedAssembler) {
        super(RenewalController.class, RenewalModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public RenewalModel toModel(@NonNull RenewalEntity entity) {
        RenewalModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(RenewalController.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(RenewalController.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<RenewalModel> toPagedModel(Page<RenewalEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    /**
     * Called internally.  Responsible for mapping a {@link RenewalEntity} to a {@link RenewalModel}.
     * Override the default behavior of using reflection
     * @param entity The {@link RenewalEntity} to be mapped
     * @return The mapped {@link RenewalModel}l
     */
    @NonNull
    @Override
    protected RenewalModel instantiateModel(@NonNull RenewalEntity entity) {
        return mapper.toModel(entity);
    }
}
