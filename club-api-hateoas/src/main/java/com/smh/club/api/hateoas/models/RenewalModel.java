package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import java.time.LocalDate;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

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
@SortTarget(RenewalEntity.class)
public class RenewalModel extends RepresentationModel<RenewalModel> {

    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("renewal-date")
    private LocalDate renewalDate;

    @JsonProperty("renewal-year")
    private int renewalYear;
}
