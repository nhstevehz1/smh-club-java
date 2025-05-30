package com.smh.club.api.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortExclude;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for phones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PhoneDto {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member_id")
    private int memberId;

    @NotBlank
    @Size(min = 1, max = 5)
    @JsonProperty("country_code")
    private String countryCode;

    @NotBlank
    @Size(min = 10, max = 10)
    @Digits(integer = 10, fraction = 0)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @JsonProperty("phone_type")
    private PhoneType phoneType;
}
