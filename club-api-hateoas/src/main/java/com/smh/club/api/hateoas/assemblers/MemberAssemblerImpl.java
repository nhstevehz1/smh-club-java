package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.contracts.assemblers.MemberAssembler;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.controllers.MemberControllerImpl;
import com.smh.club.api.hateoas.models.MemberModel;
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
public class MemberAssemblerImpl
    extends RepresentationModelAssemblerSupport<MemberEntity, MemberModel>
    implements MemberAssembler {

    private final MemberMapper mapper;
    private final PagedResourcesAssembler<MemberEntity> pagedAssembler;

    @Autowired
    public MemberAssemblerImpl(MemberMapper mapper, PagedResourcesAssembler<MemberEntity> pagedAssembler) {
        super(MemberControllerImpl.class, MemberModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    @NonNull
    @Override
    public MemberModel toModel(@NonNull MemberEntity entity) {

        MemberModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(MemberControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(MemberControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    @Override
    public PagedModel<MemberModel> toPagedModel(Page<MemberEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    @NonNull
    @Override // Don't want to use reflection
    protected MemberModel instantiateModel(@NonNull MemberEntity entity) {
        return mapper.toModel(entity);
    }

}
