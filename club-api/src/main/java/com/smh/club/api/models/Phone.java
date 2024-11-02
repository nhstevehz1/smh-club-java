package com.smh.club.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    @JsonProperty("id")
    private int id;

    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("phone-number")
    private String phoneNum;

    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
