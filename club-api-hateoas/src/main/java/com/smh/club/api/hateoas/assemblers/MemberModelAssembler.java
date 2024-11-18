package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.dto.MemberDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MemberModelAssembler implements RepresentationModelAssembler<MemberDto, EntityModel<MemberDto>> {

    @NonNull
    @Override
    public EntityModel<MemberDto> toModel(@NonNull MemberDto dto) {
        var model = EntityModel.of(dto);

        // add links

        return model;
    }

    @NonNull
    @Override
    public CollectionModel<EntityModel<MemberDto>> toCollectionModel(@NonNull Iterable<? extends MemberDto> dtoList) {
        return RepresentationModelAssembler.super.toCollectionModel(dtoList);
    }
}
