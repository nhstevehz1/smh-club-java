package com.smh.club.api.rest.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for renewals.
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class RenewalDto {

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
