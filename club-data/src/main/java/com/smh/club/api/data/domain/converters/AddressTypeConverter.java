package com.smh.club.api.data.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import smh.club.shared.api.domain.AddressType;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AddressType addressType) {
        return addressType.getCode();
    }

    @Override
    public AddressType convertToEntityAttribute(Integer code) {
        return AddressType.of(code);
    }
}
