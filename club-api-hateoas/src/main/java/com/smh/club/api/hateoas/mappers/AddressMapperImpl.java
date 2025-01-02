package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 * Extends {@link DomainDataMapper} and implements{@link AddressMapper}.
 */
@Service
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    @Autowired
    public AddressMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity toEntity(AddressModel model) {
        var entity = modelMapper.map(model, AddressEntity.class);
        entity.setId(0);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressModel toModel(AddressEntity entity) {
        return modelMapper.map(entity, AddressModel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity updateEntity(AddressModel model, AddressEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AddressModel> toModelList(List<AddressEntity> entityList) {
        return mapList(entityList, AddressModel.class);
    }
}
