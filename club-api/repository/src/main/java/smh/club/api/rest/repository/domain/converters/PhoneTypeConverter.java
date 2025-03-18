package com.smh.club.api.rest.domain.converters;

import com.smh.club.api.rest.domain.entities.PhoneType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PhoneTypeConverter implements AttributeConverter<PhoneType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PhoneType phoneType) {
        return phoneType.getCode();
    }

    @Override
    public PhoneType convertToEntityAttribute(Integer code) {
        return PhoneType.of(code);
    }
}
