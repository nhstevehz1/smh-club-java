package com.smh.club.api.data.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.smh.club.api.shared.domain.PhoneType;

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
