package com.smh.club.api.domain.converters;

import com.smh.club.api.dto.AddressType;
import jakarta.persistence.AttributeConverter;

public class AddressTypeConverter implements AttributeConverter<AddressType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AddressType addressType) {
        return addressType.getAddressId();
    }

    @Override
    public AddressType convertToEntityAttribute(Integer addressId) {
        return AddressType.getAddressType(addressId);
    }
}
