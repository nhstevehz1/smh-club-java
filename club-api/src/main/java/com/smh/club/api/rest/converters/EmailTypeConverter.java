package com.smh.club.api.rest.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import smh.club.shared.domain.EmailType;

@Converter(autoApply = true)
public class EmailTypeConverter implements AttributeConverter<EmailType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmailType emailType) {
        return emailType.getCode();
    }

    @Override
    public EmailType convertToEntityAttribute(Integer code) {
        return EmailType.of(code);
    }
}
