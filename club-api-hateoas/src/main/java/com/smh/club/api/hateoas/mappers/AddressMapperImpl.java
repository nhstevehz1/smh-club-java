package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.models.AddressModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper {

    @Autowired
    public AddressMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public AddressEntity toEntity(AddressModel model) {
        var entity = modelMapper.map(model, AddressEntity.class);
        entity.setId(0);
        return entity;
    }

    @Override
    public AddressModel toModel(AddressEntity entity) {
        return modelMapper.map(entity, AddressModel.class);
    }

    @Override
    public AddressEntity updateEntity(AddressModel model, AddressEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    @Override
    public List<AddressModel> toModelList(List<AddressEntity> entityList) {
        return mapList(entityList, AddressModel.class);
    }
}
