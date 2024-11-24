package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.annotations.SortExclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import smh.club.shared.domain.AddressType;

/**
 * DTO for addresses  When serialize to JSON, links are generated that
 * represent controller endpoints.
 * Extends {@link RepresentationModel}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressModel extends RepresentationModel<AddressModel> {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("address1")
    private String address1;

    @SortExclude
    @JsonProperty("address2")
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
