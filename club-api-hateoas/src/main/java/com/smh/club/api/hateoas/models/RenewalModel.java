package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.domain.entities.RenewalEntity;
import java.time.LocalDate;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import smh.club.shared.api.annotations.SortExclude;
import smh.club.shared.api.annotations.SortTarget;

/**
 * DTO for renewals  When serialize to JSON, links are generated that
 * represent controller endpoints.
 * Extends {@link RepresentationModel}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(target = RenewalEntity.class)
public class RenewalModel extends RepresentationModel<RenewalModel> {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @JsonProperty("renewal-year")
    private String renewalYear;
}
