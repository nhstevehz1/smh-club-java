package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.models.Email;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailMapperImpl implements EmailMapper {
    @Override
    public EmailEntity toEntity(Email dataObject) {
        return EmailEntity.builder()
                .email(dataObject.getEmail())
                .emailType(dataObject.getEmailType())
                .build();
    }

    @Override
    public Email toDataObject(EmailEntity entity) {
        return Email.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .email(entity.getEmail())
                .emailType(entity.getEmailType())
                .build();
    }

    @Override
    public void updateEntity(Email dataObject, EmailEntity entity) {
        entity.setEmail(dataObject.getEmail());
        entity.setEmailType(dataObject.getEmailType());
    }

    @Override
    public List<Email> toDataObjectList(List<EmailEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
