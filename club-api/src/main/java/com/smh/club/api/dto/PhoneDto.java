package com.smh.club.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortDefault;
import com.smh.club.api.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {

    @SortDefault
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("phone-number")
    private String phoneNum;

    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
