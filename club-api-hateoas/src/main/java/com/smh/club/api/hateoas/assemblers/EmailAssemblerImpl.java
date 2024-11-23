package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.contracts.assemblers.EmailAssembler;
import com.smh.club.api.hateoas.contracts.mappers.EmailMapper;
import com.smh.club.api.hateoas.controllers.EmailControllerImpl;
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

@Component
public class EmailAssemblerImpl
    extends RepresentationModelAssemblerSupport<EmailEntity, EmailModel>
    implements EmailAssembler {

    private final EmailMapper mapper;
    private final PagedResourcesAssembler<EmailEntity> pagedAssembler;

    @Autowired
    public EmailAssemblerImpl(EmailMapper mapper, PagedResourcesAssembler<EmailEntity> pagedAssembler) {
        super(EmailControllerImpl.class, EmailModel.class);
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
    }

    @NonNull
    @Override
    public EmailModel toModel(@NonNull EmailEntity entity) {
        EmailModel model = createModelWithId(entity.getId(), entity);

        model.add(linkTo(methodOn(EmailControllerImpl.class).update(entity.getId(), model)).withRel("update"));
        model.add(linkTo(methodOn(EmailControllerImpl.class).delete(entity.getId())).withRel("delete"));
        return model;
    }

    @Override
    public PagedModel<EmailModel> toPagedModel(Page<EmailEntity> page) {
        return pagedAssembler.toModel(page, this);
    }

    @NonNull
    @Override
    protected EmailModel instantiateModel(@NonNull EmailEntity entity) {
        return mapper.toModel(entity);
    }
}
