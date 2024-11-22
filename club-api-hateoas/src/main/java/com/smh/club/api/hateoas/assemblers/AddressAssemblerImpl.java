package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.contracts.assemblers.AddressAssembler;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.controllers.AddressControllerImpl;
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

@Component
public class AddressAssemblerImpl
    extends RepresentationModelAssemblerSupport<AddressEntity, AddressModel>
    implements AddressAssembler {

    private final AddressMapper mapper;
    private final PagedResourcesAssembler<AddressEntity> pagedAssembler;

    @Autowired
    public AddressAssemblerImpl(AddressMapper mapper, PagedResourcesAssembler<AddressEntity> pagedAssembler) {
        super(AddressControllerImpl.class, AddressModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    @NonNull
    @Override
    public AddressModel toModel(@NonNull AddressEntity entity) {
        AddressModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(AddressControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(AddressControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    @Override
    public PagedModel<AddressModel> toPagedModel(Page<AddressEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    @NonNull
    @Override
    protected AddressModel instantiateModel(@NonNull AddressEntity entity) {
        return mapper.toModel(entity);
    }
}
