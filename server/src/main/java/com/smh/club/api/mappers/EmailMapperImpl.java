package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.EmailMapper;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.email.EmailMemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
    public EmailMemberDto toEmailMemberDto(EmailEntity entity) {
        return modelMapper.map(entity, EmailMemberDto.class);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EmailMemberDto> toPage(Page<EmailEntity> page) {
        return page.map(this::toEmailMemberDto);
    }
}
