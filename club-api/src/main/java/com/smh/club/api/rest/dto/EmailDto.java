package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.annotations.SortExclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import smh.club.shared.domain.EmailType;

/**
 * DTO for emails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {

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
