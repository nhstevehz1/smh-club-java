package com.smh.club.api.rest.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.validation.constraints.BirthDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for members.
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class MemberBaseDto {

    @JsonProperty("member_number")
    private int memberNumber;

    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @SortExclude
    @JsonProperty("middle_name")
    private String middleName;

    @NotBlank
    @JsonProperty("last_name")
    private String lastName;

    @SortExclude
    @JsonProperty("suffix")
    private String suffix;

    @NotNull
    @BirthDate
    @JsonProperty("birth_date")
    private Instant birthDate;

    @NotNull
    @PastOrPresent
    @JsonProperty("joined_date")
    private Instant joinedDate;

}
