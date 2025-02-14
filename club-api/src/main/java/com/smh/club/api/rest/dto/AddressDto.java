package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.domain.entities.AddressType;
import com.smh.club.api.rest.validation.constraints.PostalCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for addresses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(AddressEntity.class)
public class AddressDto {
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member_id")
    private int memberId;

    @NotBlank()
    @JsonProperty("address1")
    private String address1;

    @SortExclude
    @JsonProperty("address2")
    private String address2;

    @NotBlank()
    @JsonProperty("city")
    private String city;

    @NotBlank()
    @JsonProperty("state")
    private String state;

    @PostalCode
    @NotBlank
    @JsonProperty("zip")
    private String zip;

    @NotNull()
    @JsonProperty("address_type")
    private AddressType addressType;
}
