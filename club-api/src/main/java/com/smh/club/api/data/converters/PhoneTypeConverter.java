package com.smh.club.api.data.converters;

import com.smh.club.api.models.PhoneType;
import jakarta.persistence.AttributeConverter;

public class PhoneTypeConverter implements AttributeConverter<PhoneType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PhoneType phoneType) {
        return phoneType.getPhoneId();
    }

    @Override
    public PhoneType convertToEntityAttribute(Integer phoneId) {
        return PhoneType.getPhoneType(phoneId);
    }
}
