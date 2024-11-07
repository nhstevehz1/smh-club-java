package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.dto.update.UpdateEmailDto;

import java.util.List;

public interface EmailMapper {
    EmailEntity toEntity(CreateEmailDto createEmailDto);
    EmailDto toDto(EmailEntity entity);
    EmailEntity updateEntity(UpdateEmailDto updateEmailDto, EmailEntity entity);
    List<EmailDto> toDtoList(List<EmailEntity> entityList);
}
