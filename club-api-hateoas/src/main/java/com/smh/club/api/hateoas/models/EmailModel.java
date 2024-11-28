package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.domain.entities.EmailEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import smh.club.shared.api.annotations.SortExclude;
import smh.club.shared.api.annotations.SortTarget;
import smh.club.shared.api.domain.EmailType;

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
@SortTarget(target = EmailEntity.class)
public class EmailModel extends RepresentationModel<EmailModel> {
    @JsonProperty("id")
    private int id;

    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @NotEmpty
    @JsonProperty("email")
    private String email;

    @JsonProperty("email-type")
    private EmailType emailType;
}
