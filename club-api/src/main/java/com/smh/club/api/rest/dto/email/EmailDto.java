package com.smh.club.api.rest.dto.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO for emails.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EmailDto extends EmailBaseDto{

    public EmailDto() {
        super();
    }

    @JsonProperty("id")
    private int id;
}
