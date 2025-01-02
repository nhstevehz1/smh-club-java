package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 * Extends {@link DomainDataMapper} and implements{@link PhoneMapper}.
 */
@Service
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    @Autowired
    public PhoneMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity toEntity(PhoneModel model) {
        var entity = modelMapper.map(model, PhoneEntity.class);
        entity.setId(0);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneModel toModel(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneModel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity updateEntity(PhoneModel model, PhoneEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhoneModel> toModelList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneModel.class);
    }
}
