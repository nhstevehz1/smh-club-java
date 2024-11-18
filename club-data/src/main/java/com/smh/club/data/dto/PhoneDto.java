package com.smh.club.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.data.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
