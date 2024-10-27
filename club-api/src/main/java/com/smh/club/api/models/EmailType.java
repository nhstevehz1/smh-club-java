package com.smh.club.api.models;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EmailType {

    Home(0, EmailTypeNames.HOME),
    Work(1, EmailTypeNames.WORK),
    Other(2, EmailTypeNames.OTHER);

    private final int emailId;
    private final String emailName;

    EmailType(int emailId, String emailName) {
        this.emailId = emailId;
        this.emailName = emailName;
    }

    public static EmailType getEmailType(int emailId) {
        return Arrays.stream(EmailType.values())
                .filter(v -> v.getEmailId() == emailId)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static class EmailTypeNames extends TypeNamesBase{
        // add more email type names if needed
    }
}
