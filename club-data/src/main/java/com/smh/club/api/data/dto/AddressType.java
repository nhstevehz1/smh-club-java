package com.smh.club.api.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Stream;

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
