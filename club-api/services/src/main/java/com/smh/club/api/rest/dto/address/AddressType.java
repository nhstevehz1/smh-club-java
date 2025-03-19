package com.smh.club.api.rest.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * Address type enum
 */
@Getter
public enum AddressType {

    @JsonProperty(Names.HOME)
    Home(0, Names.HOME),

    @JsonProperty(Names.WORK)
    Work(1, Names.WORK),

    @JsonProperty(Names.OTHER)
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
