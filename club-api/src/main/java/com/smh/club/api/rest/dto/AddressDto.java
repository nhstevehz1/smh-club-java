package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import com.smh.club.api.shared.domain.AddressType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Min(1)
    @SortExclude
    @JsonProperty("member-id")
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

    @Size(min = 5, max = 9)
    @NotBlank
    @JsonProperty("zip")
    private String zip;

    @NotNull()
    @JsonProperty("address-type")
    private AddressType addressType;
}
