package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.PhoneEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import com.smh.club.api.shared.domain.PhoneType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Min(1)
    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotBlank
    @JsonProperty("phone-number")
    private String phoneNumber;

    @NotNull
    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
