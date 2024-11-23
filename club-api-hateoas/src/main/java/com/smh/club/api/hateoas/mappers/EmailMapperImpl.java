package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.contracts.mappers.EmailMapper;
import com.smh.club.api.hateoas.models.EmailModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailMapperImpl extends DomainDataMapper implements EmailMapper {

    @Autowired
    public EmailMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public EmailEntity toEntity(EmailModel model) {
        var entity = modelMapper.map(model, EmailEntity.class);
        entity.setId(0);
        return entity;
    }

    @Override
    public EmailModel toModel(EmailEntity entity) {
        return modelMapper.map(entity, EmailModel.class);
    }

    @Override
    public EmailEntity updateEntity(EmailModel model, EmailEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    @Override
    public List<EmailModel> toModelList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailModel.class);
    }

}
