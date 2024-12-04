package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.rest.validation.constraints.ValidMember;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import com.smh.club.api.shared.validators.constraints.BirthDate;
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
    @JsonProperty("member-number")
    private int memberNumber;

    @NotBlank
    @JsonProperty("first-name")
    private String firstName;

    @SortExclude
    @JsonProperty("middle-name")
    private String middleName;

    @NotBlank
    @JsonProperty("last-name")
    private String lastName;

    @SortExclude
    @JsonProperty("suffix")
    private String suffix;

    @NotNull
    @BirthDate
    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @NotNull
    @PastOrPresent
    @JsonProperty("joined-date")
    private LocalDate joinedDate;

}
