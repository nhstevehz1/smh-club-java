package com.smh.club.api.rest.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import smh.club.shared.domain.AddressType;

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
