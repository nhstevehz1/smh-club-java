package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for renewals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(RenewalEntity.class)
public class RenewalDto {

    @JsonProperty("id")
    private int id;

    @Min(1)
    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotNull
    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @NotNull
    @JsonProperty("renewal-year")
    private String renewalYear;
}
