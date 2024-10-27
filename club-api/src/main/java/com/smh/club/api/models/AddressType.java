package com.smh.club.api.models;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AddressType {

    Home(0, AddressTypeNames.HOME),
    Work(1, AddressTypeNames.WORK),
    Other(2,AddressTypeNames.OTHER );

    private final int addressId;
    private final String addressName;

    AddressType(int addressId, String addressName) {
        this.addressId = addressId;
        this.addressName = addressName;
    }

    public static AddressType getAddressType(int addressId) {
        return Arrays.stream(AddressType.values())
                .filter(v -> v.getAddressId() == addressId)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static class AddressTypeNames extends TypeNamesBase {
        // add more type names if needed.
    }
}
