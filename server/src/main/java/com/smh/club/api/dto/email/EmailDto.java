package com.smh.club.api.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortExclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for emails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmailDto {

    @JsonProperty("id")
    private int id;

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
