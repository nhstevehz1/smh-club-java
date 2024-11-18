package com.smh.club.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.data.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("member-number")
    private int memberNumber;

    @JsonProperty("first-name")
    private String firstName;

    @SortExclude
    @JsonProperty("middle-name")
    private String middleName;

    @JsonProperty("last-name")
    private String lastName;

    @SortExclude
    @JsonProperty("suffix")
    private String suffix;

    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @JsonProperty("joined-date")
    private LocalDate joinedDate;

}
