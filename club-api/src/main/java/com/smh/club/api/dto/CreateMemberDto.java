package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberDto {

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
