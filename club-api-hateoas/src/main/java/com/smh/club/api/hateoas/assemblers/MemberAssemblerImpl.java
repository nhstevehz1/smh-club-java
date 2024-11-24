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

/**
 * {@inheritDoc}
 * Extends {@link RepresentationModelAssemblerSupport}
 */
@Component
public class MemberAssemblerImpl
    extends RepresentationModelAssemblerSupport<MemberEntity, MemberModel>
    implements MemberAssembler {

    private final MemberMapper mapper;
    private final PagedResourcesAssembler<MemberEntity> pagedAssembler;

    /**
        * Constructor.
     * @param mapper An {@link MemberMapper
    } used to copy the properties to an {@link MemberModel}
     *               from an {@link MemberEntity
    }.
        * @param pagedAssembler A {@link PagedResourcesAssembler} of type {@link MemberEntity} used to
     *                       copy a {@link Page} to a {@link PagedModel}.
        * @see MemberAssemblerImpl#toPagedModel                   
     */
    @Autowired
    public MemberAssemblerImpl(MemberMapper mapper, PagedResourcesAssembler<MemberEntity> pagedAssembler) {
        super(MemberControllerImpl.class, MemberModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public MemberModel toModel(@NonNull MemberEntity entity) {

        MemberModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(MemberControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(MemberControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<MemberModel> toPagedModel(Page<MemberEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    /**
     * Called internally.  Responsible for mapping an {@link MemberEntity} to a {@link MemberModel}.
     * Override the default behavior of using reflection
     * @param entity The {@link MemberEntity} to be mapped
     * @return The mapped {@link MemberModel}l
     */
    @NonNull
    @Override // Don't want to use reflection
    protected MemberModel instantiateModel(@NonNull MemberEntity entity) {
        return mapper.toModel(entity);
    }

}
