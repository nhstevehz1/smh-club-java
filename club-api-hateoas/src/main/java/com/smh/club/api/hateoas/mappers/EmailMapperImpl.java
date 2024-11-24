package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.contracts.mappers.EmailMapper;
import com.smh.club.api.hateoas.models.EmailModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@inheritDoc}
 * Extends {@link DomainDataMapper} and implements{@link EmailMapper}.
 */
@Service
public class EmailMapperImpl extends DomainDataMapper implements EmailMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    @Autowired
    public EmailMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEntity toEntity(EmailModel model) {
        var entity = modelMapper.map(model, EmailEntity.class);
        entity.setId(0);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailModel toModel(EmailEntity entity) {
        return modelMapper.map(entity, EmailModel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEntity updateEntity(EmailModel model, EmailEntity entity) {
        modelMapper.map(model, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmailModel> toModelList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailModel.class);
    }

}
