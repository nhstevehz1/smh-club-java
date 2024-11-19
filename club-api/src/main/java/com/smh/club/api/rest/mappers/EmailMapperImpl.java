package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.contracts.mappers.EmailMapper;
import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.dto.EmailDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailMapperImpl extends DomainDataMapper implements EmailMapper {

    public EmailMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public EmailEntity toEntity(EmailDto createEmailDto) {
        return modelMapper.map(createEmailDto, EmailEntity.class);
    }

    @Override
    public EmailDto toDto(EmailEntity entity) {
        return modelMapper.map(entity, EmailDto.class);
    }

    @Override
    public EmailEntity updateEntity(EmailDto updateEmailDto, EmailEntity entity) {
        modelMapper.map(updateEmailDto, entity);
        return entity;
    }

    @Override
    public List<EmailDto> toDtoList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailDto.class);
    }
}
