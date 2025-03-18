package com.smh.club.api.rest.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO for addresses.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AddressDto extends AddressBaseDto {
    @JsonProperty("id")
    private int id;

    public AddressDto() {
        super();
    }
}
