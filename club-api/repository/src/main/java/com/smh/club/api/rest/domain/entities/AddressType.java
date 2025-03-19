package com.smh.club.api.rest.domain.entities;

import java.util.stream.Stream;
import lombok.Getter;

/**
 * Address type enum
 */
@Getter
public enum AddressType {
    Home(0, Names.HOME),
    Work(1, Names.WORK),
    Other(2, Names.OTHER);

    private final int code;
    private final String addressTypeName;

    AddressType(int code, String addressTypeName) {
        this.code = code;
        this.addressTypeName = addressTypeName;
    }

    /**
     * Returns an address type based on its code.
     */
    public static AddressType of (int code) {
        return Stream.of(AddressType.values())
                .filter(a -> a.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static class Names {
        public final static String HOME = "Home";
        public final static String WORK = "Work";
        public final static String OTHER = "Other";
    }
}
