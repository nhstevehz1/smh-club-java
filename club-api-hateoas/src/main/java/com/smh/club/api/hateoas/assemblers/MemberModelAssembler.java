package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.hateoas.controllers.MemberControllerImpl;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MemberModelAssembler implements RepresentationModelAssembler<MemberDto, EntityModel<MemberDto>> {

    @NonNull
    @Override
    public EntityModel<MemberDto> toModel(@NonNull MemberDto dto) {
        var model = EntityModel.of(dto);

        // link to self
        model.add(linkTo(methodOn(MemberControllerImpl.class).get(dto.getId())).withSelfRel());
        // link to update
        model.add(linkTo(methodOn(MemberControllerImpl.class).update(dto.getId(), dto)).withRel("update"));
        // link to delete
        model.add((linkTo(methodOn(MemberControllerImpl.class).delete(dto.getId()))).withRel("delete"));
        // link to addresses
        // link to emails
        // link to phones
        // link to renewals

        return model;
    }

    @NonNull
    @Override
    public CollectionModel<EntityModel<MemberDto>> toCollectionModel(@NonNull Iterable<? extends MemberDto> dtoList) {
        return RepresentationModelAssembler.super.toCollectionModel(dtoList);
    }
}
