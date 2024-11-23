package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.models.PhoneModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    @Autowired
    public PhoneMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public PhoneEntity toEntity(PhoneModel model) {
        var entity = modelMapper.map(model, PhoneEntity.class);
        entity.setId(0);
        return entity;
    }

    @Override
    public PhoneModel toModel(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneModel.class);
    }

    @Override
    public PhoneEntity updateEntity(PhoneModel model, PhoneEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    @Override
    public List<PhoneModel> toModelList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneModel.class);
    }
}
