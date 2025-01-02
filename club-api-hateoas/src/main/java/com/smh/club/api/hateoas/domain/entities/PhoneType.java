package com.smh.club.api.hateoas.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * A phone type enum.
 */
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

    /**
     * Returns an address based on its code.
     */
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
