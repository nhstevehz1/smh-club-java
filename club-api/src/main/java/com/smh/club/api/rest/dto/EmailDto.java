package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import com.smh.club.api.shared.domain.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for emails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(EmailEntity.class)
public class EmailDto {

    @JsonProperty("id")
    private int id;

    @Min(1)
    @SortExclude
    @JsonProperty("member-id")
    private int memberId;

    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("email-type")
    private EmailType emailType;
}
