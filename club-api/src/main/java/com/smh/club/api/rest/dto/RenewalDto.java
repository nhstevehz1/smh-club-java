package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.RenewalEntity;
import com.smh.club.api.rest.validation.constraints.ValidRenewal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
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
    @JsonProperty("member_id")
    private int memberId;

    @NotNull
    @PastOrPresent
    @JsonProperty("renewal_date")
    private Instant renewalDate;

    @JsonProperty("renewal_year")
    private int renewalYear;
}
