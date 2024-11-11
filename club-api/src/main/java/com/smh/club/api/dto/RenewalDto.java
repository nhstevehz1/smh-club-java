package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortDefault;
import com.smh.club.api.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewalDto {

    @SortDefault
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @JsonProperty("renewal-year")
    private String renewalYear;
}
