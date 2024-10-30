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
public class Address {
    @JsonProperty("id")
    private int id;

    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("address-1")
    private String address1;

    @JsonProperty("address-2")
    private String address2;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("address-type")
    private AddressType addressType;
}
