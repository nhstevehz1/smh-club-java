package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.rest.dto.EmailDto;

import java.util.List;

public interface EmailMapper {
    EmailEntity toEntity(EmailDto createEmailDto);
    EmailDto toDto(EmailEntity entity);
    EmailEntity updateEntity(EmailDto updateEmailDto, EmailEntity entity);
    List<EmailDto> toDtoList(List<EmailEntity> entityList);
}
