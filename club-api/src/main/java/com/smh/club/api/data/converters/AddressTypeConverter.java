package com.smh.club.api.data.converters;

import com.smh.club.api.models.AddressType;
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
