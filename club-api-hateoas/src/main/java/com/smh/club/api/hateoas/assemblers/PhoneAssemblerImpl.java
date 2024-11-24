package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.contracts.assemblers.PhoneAssembler;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.controllers.PhoneControllerImpl;
import com.smh.club.api.hateoas.models.PhoneModel;
import com.smh.club.api.hateoas.models.PhoneModel;
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
public class PhoneAssemblerImpl extends RepresentationModelAssemblerSupport<PhoneEntity, PhoneModel>
    implements PhoneAssembler {

    private final PhoneMapper mapper;
    private final PagedResourcesAssembler<PhoneEntity> pagedAssembler;

    /**
     * Constructor.
     * @param mapper An {@link PhoneMapper} used to copy the properties to an {@link PhoneModel}
     *               from an {@link PhoneEntity}.
     * @param pagedAssembler A {@link PagedResourcesAssembler} of type {@link PhoneEntity} used to
     *                       copy a {@link Page} to a {@link PagedModel}.
     * @see PhoneAssemblerImpl#toPagedModel
     */
    @Autowired
    public PhoneAssemblerImpl(PhoneMapper mapper, PagedResourcesAssembler<PhoneEntity> pagedAssembler) {
        super(PhoneControllerImpl.class, PhoneModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PhoneModel toModel(@NonNull PhoneEntity entity) {
        PhoneModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(PhoneControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(PhoneControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<PhoneModel> toPagedModel(Page<PhoneEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    /**
     * Called internally.  Responsible for mapping an {@link PhoneEntity} to a {@link PhoneModel}.
     * Override the default behavior of using reflection
     * @param entity The {@link PhoneEntity} to be mapped
     * @return The mapped {@link PhoneModel}l
     */
    @NonNull
    @Override
    protected PhoneModel instantiateModel(@NonNull PhoneEntity entity) {
        return mapper.toModel(entity);
    }
}
