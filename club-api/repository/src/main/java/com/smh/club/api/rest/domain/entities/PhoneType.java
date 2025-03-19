package com.smh.club.api.rest.domain.entities;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * A phone type enum.
 */
@Getter
public enum PhoneType {
    Home(0, Names.HOME),
    Work(1, Names.WORK),
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
