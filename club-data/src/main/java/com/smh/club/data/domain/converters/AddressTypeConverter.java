package com.smh.club.data.domain.converters;

import com.smh.club.data.dto.AddressType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
