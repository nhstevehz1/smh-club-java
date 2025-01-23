package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.validation.constraints.BirthDate;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for members.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidMember
@SortTarget(MemberEntity.class)
public class MemberDto {
    @JsonProperty("id")
    private int id;

    @Min(1)
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
    private LocalDate birthDate;

    @NotNull
    @PastOrPresent
    @JsonProperty("joined_date")
    private LocalDate joinedDate;

}
