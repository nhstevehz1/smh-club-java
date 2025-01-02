package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.hateoas.contracts.assemblers.AddressAssembler;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.controllers.AddressController;
import com.smh.club.api.hateoas.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;
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
public class AddressAssemblerImpl
    extends RepresentationModelAssemblerSupport<AddressEntity, AddressModel>
    implements AddressAssembler {

    private final AddressMapper mapper;
    private final PagedResourcesAssembler<AddressEntity> pagedAssembler;

    /**
     * Constructor.
     * @param mapper An {@link AddressMapper} used to copy the properties to an {@link AddressModel}
     *               from an {@link AddressEntity}.
     * @param pagedAssembler A {@link PagedResourcesAssembler} of type {@link AddressEntity} used to
     *                       copy a {@link Page} to a {@link PagedModel}.
     * @see AddressAssemblerImpl#toPagedModel
     */
    @Autowired
    public AddressAssemblerImpl(AddressMapper mapper, PagedResourcesAssembler<AddressEntity> pagedAssembler) {
        super(AddressController.class, AddressModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public AddressModel toModel(@NonNull AddressEntity entity) {
        AddressModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(AddressController.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(AddressController.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<AddressModel> toPagedModel(Page<AddressEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    /**
     * Called internally.  Responsible for mapping an {@link AddressEntity} to a {@link AddressModel}.
     * Override the default behavior of using reflection
     * @param entity The {@link AddressEntity} to be mapped
     * @return The mapped {@link AddressModel}l
     */
    @NonNull
    @Override
    protected AddressModel instantiateModel(@NonNull AddressEntity entity) {
        return mapper.toModel(entity);
    }
}
