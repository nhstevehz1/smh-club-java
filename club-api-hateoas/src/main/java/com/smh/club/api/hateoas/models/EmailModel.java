package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import smh.club.shared.annotations.SortExclude;
import smh.club.shared.domain.EmailType;

/**
 * DTO for emails.  When serialize to JSON, links are generated that
 * represent controller endpoints.
 * Extends {@link RepresentationModel}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailModel extends RepresentationModel<EmailModel> {
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email-type")
    private EmailType emailType;
}
