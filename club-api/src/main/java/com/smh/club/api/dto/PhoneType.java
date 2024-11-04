package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PhoneType {

    @JsonProperty(PhoneTypeNames.HOME)
    Home(0, PhoneTypeNames.HOME),

    @JsonProperty(PhoneTypeNames.WORK)
    Work(1, PhoneTypeNames.WORK),

    @JsonProperty(PhoneTypeNames.OTHER)
    Other(2, PhoneTypeNames.OTHER);

    private final int phoneId;
    private final String phoneName;

    PhoneType(int phoneId, String phoneName) {
        this.phoneId = phoneId;
        this.phoneName = phoneName;
    }

    public static PhoneType getPhoneType(int phoneId) {
        return Arrays.stream(PhoneType.values())
                .filter(v -> v.getPhoneId() == phoneId)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static class PhoneTypeNames  extends TypeNamesBase {
        // add phone type names if needed
    }
}
