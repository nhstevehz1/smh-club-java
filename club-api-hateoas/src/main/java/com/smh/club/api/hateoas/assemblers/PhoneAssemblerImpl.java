package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.contracts.assemblers.PhoneAssembler;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.controllers.PhoneControllerImpl;
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

@Component
public class PhoneAssemblerImpl extends RepresentationModelAssemblerSupport<PhoneEntity, PhoneModel>
    implements PhoneAssembler {

    private final PhoneMapper mapper;
    private final PagedResourcesAssembler<PhoneEntity> pagedAssembler;

    @Autowired
    public PhoneAssemblerImpl(PhoneMapper mapper, PagedResourcesAssembler<PhoneEntity> pagedAssembler) {
        super(PhoneControllerImpl.class, PhoneModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    @NonNull
    @Override
    public PhoneModel toModel(@NonNull PhoneEntity entity) {
        PhoneModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(PhoneControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(PhoneControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    @Override
    public PagedModel<PhoneModel> toPagedModel(Page<PhoneEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    @NonNull
    @Override
    protected PhoneModel instantiateModel(@NonNull PhoneEntity entity) {
        return mapper.toModel(entity);
    }
}
