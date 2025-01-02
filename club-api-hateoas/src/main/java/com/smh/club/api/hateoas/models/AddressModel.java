package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.hateoas.domain.annotations.SortExclude;
import com.smh.club.api.hateoas.domain.annotations.SortTarget;
import com.smh.club.api.hateoas.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.domain.entities.AddressType;
import com.smh.club.api.hateoas.validation.constraints.PostalCode;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

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

    @NotBlank
    @JsonProperty("address1")
    private String address1;

    @SortExclude
    @JsonProperty("address2")
    private String address2;

    @NotBlank
    @JsonProperty("city")
    private String city;

    @NotBlank
    @JsonProperty("state")
    private String state;

    @NotBlank
    @PostalCode
    @JsonProperty("zip")
    private String zip;

    @JsonProperty("address-type")
    private AddressType addressType;
}
