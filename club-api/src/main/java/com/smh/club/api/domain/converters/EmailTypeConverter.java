package com.smh.club.api.domain.converters;

import com.smh.club.api.dto.EmailType;
import jakarta.persistence.AttributeConverter;

public class EmailTypeConverter implements AttributeConverter<EmailType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmailType emailType) {
        return emailType.getEmailId();
    }

    @Override
    public EmailType convertToEntityAttribute(Integer emailId) {
        return EmailType.getEmailType(emailId);
    }
}
