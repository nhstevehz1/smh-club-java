package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.dto.update.UpdateEmailDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EmailMapperImpl extends DomainDataMapper implements EmailMapper {

    public EmailMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public EmailEntity toEntity(CreateEmailDto createEmailDto) {
        return modelMapper.map(createEmailDto, EmailEntity.class);
    }

    @Override
    public EmailDto toDto(EmailEntity entity) {
        return modelMapper.map(entity, EmailDto.class);
    }

    @Override
    public EmailEntity updateEntity(UpdateEmailDto updateEmailDto, EmailEntity entity) {
        modelMapper.map(updateEmailDto, entity);
        return entity;
    }

    @Override
    public List<EmailDto> toDtoList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailDto.class);
    }
}
