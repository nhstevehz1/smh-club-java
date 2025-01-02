package com.smh.club.api.hateoas.domain.converters;

import com.smh.club.api.hateoas.domain.entities.AddressType;
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
