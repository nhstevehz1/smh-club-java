package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.EmailCreateDto;
import com.smh.club.api.dto.EmailDto;

import java.util.List;

public interface EmailMapper {
    EmailEntity toEntity(EmailCreateDto cemailCreateDto);
    EmailDto toDto(EmailEntity entity);
    EmailEntity updateEntity(EmailCreateDto dataObject, EmailEntity entity);
    List<EmailDto> toDtoList(List<EmailEntity> entityList);
}
