package com.smh.club.api.rest.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.rest.contracts.mappers.EmailMapper;
import com.smh.club.api.rest.dto.EmailDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import smh.club.shared.mappers.DomainDataMapper;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Component
public class EmailMapperImpl extends DomainDataMapper implements EmailMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public EmailMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEntity toEntity(EmailDto dto) {
        return modelMapper.map(dto, EmailEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailDto toDto(EmailEntity entity) {
        return modelMapper.map(entity, EmailDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailEntity updateEntity(EmailDto dto, EmailEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmailDto> toDtoList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailDto.class);
    }
}
