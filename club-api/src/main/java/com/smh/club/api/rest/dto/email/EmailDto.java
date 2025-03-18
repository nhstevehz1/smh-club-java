package com.smh.club.api.rest.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.entities.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for emails.
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class EmailDto {

    @SortExclude
    @JsonProperty("member_id")
    private int memberId;

    @NotEmpty
    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("email_type")
    private EmailType emailType;
}
