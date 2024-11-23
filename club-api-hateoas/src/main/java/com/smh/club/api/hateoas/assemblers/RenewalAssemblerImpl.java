package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.contracts.assemblers.RenewalAssembler;
import com.smh.club.api.hateoas.contracts.mappers.RenewalMapper;
import com.smh.club.api.hateoas.controllers.RenewalControllerImpl;
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

@Component
public class RenewalAssemblerImpl extends RepresentationModelAssemblerSupport<RenewalEntity, RenewalModel>
    implements RenewalAssembler {

    private final RenewalMapper mapper;
    private final PagedResourcesAssembler<RenewalEntity> pagedAssembler;

    @Autowired
    public RenewalAssemblerImpl(RenewalMapper mapper, PagedResourcesAssembler<RenewalEntity> pagedAssembler) {
        super(RenewalControllerImpl.class, RenewalModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    @NonNull
    @Override
    public RenewalModel toModel(@NonNull RenewalEntity entity) {
        RenewalModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(RenewalControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(RenewalControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    @Override
    public PagedModel<RenewalModel> toPagedModel(Page<RenewalEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    @NonNull
    @Override
    protected RenewalModel instantiateModel(@NonNull RenewalEntity entity) {
        return mapper.toModel(entity);
    }
}
