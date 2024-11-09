package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PhoneType {

    @JsonProperty(Names.HOME)
    Home(0, Names.HOME),

    @JsonProperty(Names.WORK)
    Work(1, Names.WORK),

    @JsonProperty(Names.Mobile)
    Mobile(2, Names.Mobile);

    private final int code;
    private final String phoneTypeName;

    PhoneType(int code, String phoneTypeName) {
        this.code = code;
        this.phoneTypeName = phoneTypeName;
    }

    public static PhoneType of (int code) {
        return Stream.of(PhoneType.values())
                .filter(a -> a.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static class Names {
        public final static String HOME = "Home";
        public final static String WORK = "Work";
        public final static String Mobile = "Mobile";
    }
}
