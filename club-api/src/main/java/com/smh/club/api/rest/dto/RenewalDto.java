package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@ValidRenewal
@SortTarget(RenewalEntity.class)
public class RenewalDto {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotNull
    @PastOrPresent
    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @JsonProperty("renewal-year")
    private int renewalYear;
}
