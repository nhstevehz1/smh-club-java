package com.smh.club.api.data.converters;

import com.smh.club.api.shared.domain.EmailType;
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
