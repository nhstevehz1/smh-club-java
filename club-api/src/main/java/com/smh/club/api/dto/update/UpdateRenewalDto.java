package com.smh.club.api.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRenewalDto {

    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @JsonProperty("renewal-year")
    private String renewalYear;
}
