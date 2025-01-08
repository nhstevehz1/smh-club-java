package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.rest.domain.entities.PhoneType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for phones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(PhoneEntity.class)
public class PhoneDto {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member_id")
    private int memberId;

    @NotBlank
    @Size(min = 10, max = 10)
    @Digits(integer = 10, fraction = 0)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @JsonProperty("phone_type")
    private PhoneType phoneType;
}
