package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.models.MemberModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import smh.club.shared.api.mappers.DomainDataMapper;

import java.util.List;

/**
 * {@inheritDoc}
 * Extends {@link DomainDataMapper} and implements{@link MemberMapper}.
 */
@Service
public class MemberMapperImpl extends DomainDataMapper implements MemberMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public MemberMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity toEntity(MemberModel model) {
        var entity = modelMapper.map(model, MemberEntity.class);
        entity.setId(0);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberModel toModel(MemberEntity entity) {
        return modelMapper.map(entity, MemberModel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity updateEntity(MemberModel model, MemberEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberModel> toModelList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberModel.class);
    }
}
