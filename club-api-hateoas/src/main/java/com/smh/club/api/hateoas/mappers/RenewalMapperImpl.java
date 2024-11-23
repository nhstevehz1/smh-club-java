package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.contracts.mappers.RenewalMapper;
import com.smh.club.api.hateoas.models.RenewalModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenewalMapperImpl extends DomainDataMapper implements RenewalMapper {

    @Autowired
    public RenewalMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public RenewalEntity toEntity(RenewalModel model) {
        var entity = modelMapper.map(model, RenewalEntity.class);
        entity.setId(0);
        return entity;
    }

    @Override
    public RenewalModel toModel(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalModel.class);
    }

    @Override
    public RenewalEntity updateEntity(RenewalModel model, RenewalEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    @Override
    public List<RenewalModel> toModelList(List<RenewalEntity> entityList) {
        return mapList(entityList, RenewalModel.class);
    }
}
