package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.hateoas.domain.annotations.SortExclude;
import com.smh.club.api.hateoas.domain.annotations.SortTarget;
import com.smh.club.api.hateoas.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.domain.entities.PhoneType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO for phones.  When serialize to JSON, links are generated that
 * represent controller endpoints.
 * Extends {@link RepresentationModel}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(PhoneEntity.class)
public class PhoneModel extends RepresentationModel<PhoneModel> {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotBlank
    @Size(min = 10, max = 10)
    @Digits(integer = 10, fraction = 0)
    @JsonProperty("phone-number")
    private String phoneNumber;

    @NotNull
    @JsonProperty("phone-type")
    private PhoneType phoneType;
}
