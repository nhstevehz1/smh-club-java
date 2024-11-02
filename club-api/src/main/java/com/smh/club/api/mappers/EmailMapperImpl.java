package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.data.dto.EmailDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailMapperImpl implements EmailMapper {
    @Override
    public EmailEntity toEntity(EmailDto dataObject) {
        return EmailEntity.builder()
                .email(dataObject.getEmail())
                .emailType(dataObject.getEmailType())
                .build();
    }

    @Override
    public EmailDto toDataObject(EmailEntity entity) {
        return EmailDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .email(entity.getEmail())
                .emailType(entity.getEmailType())
                .build();
    }

    @Override
    public EmailEntity updateEntity(EmailDto dataObject, EmailEntity entity) {
        entity.setEmail(dataObject.getEmail());
        entity.setEmailType(dataObject.getEmailType());
        return entity;
    }

    @Override
    public List<EmailDto> toDataObjectList(List<EmailEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
