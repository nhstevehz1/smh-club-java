package com.smh.club.api.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortExclude;
import com.smh.club.api.validation.constraints.ValidRenewal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * DTO for renewals.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ValidRenewal
public class RenewalDto {
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member_id")
    private int memberId;

    @NotNull
    @PastOrPresent
    @JsonProperty("renewal_date")
    private Instant renewalDate;

    @JsonProperty("renewal_year")
    private int renewalYear;
}
