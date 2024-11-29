package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.domain.entities.AddressEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import smh.club.shared.api.annotations.SortExclude;
import smh.club.shared.api.annotations.SortTarget;
import smh.club.shared.api.domain.AddressType;

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
@SortTarget(AddressEntity.class)
public class AddressModel extends RepresentationModel<AddressModel> {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotEmpty
    @JsonProperty("address1")
    private String address1;

    @SortExclude
    @JsonProperty("address2")
    private String address2;

    @NotEmpty
    @JsonProperty("city")
    private String city;

    @NotEmpty
    @JsonProperty("state")
    private String state;

    @NotEmpty
    @JsonProperty("zip")
    private String zip;

    @JsonProperty("address-type")
    private AddressType addressType;
}
