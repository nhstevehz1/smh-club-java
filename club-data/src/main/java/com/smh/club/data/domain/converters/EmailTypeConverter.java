package com.smh.club.data.domain.converters;

import com.smh.club.data.dto.EmailType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
