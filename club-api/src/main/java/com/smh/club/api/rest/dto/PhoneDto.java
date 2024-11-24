package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import smh.club.shared.domain.PhoneType;

/**
 * DTO for phones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("phone-number")
    private String phoneNumber;

    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
