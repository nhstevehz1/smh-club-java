package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @JsonProperty("joined-date")
    private LocalDate joinedDate;

}
