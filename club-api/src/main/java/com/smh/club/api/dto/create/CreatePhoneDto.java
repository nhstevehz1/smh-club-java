package com.smh.club.api.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.dto.PhoneType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePhoneDto {
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("phone-number")
    private String phoneNum;

    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
