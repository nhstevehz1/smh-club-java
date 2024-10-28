package com.smh.club.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Member {
    @JsonProperty("id")
    private int id;

    @JsonProperty("member-number")
    private int memberNumber;

    @JsonProperty("first-name")
    private String firstName;

    @JsonProperty("middle-name")
    private String middleName;

    @JsonProperty("last-name")
    private String lastName;

    @JsonProperty("suffix")
    private String suffix;

    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @JsonProperty("joined-date")
    private LocalDate joinedDate;

}
