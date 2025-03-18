package com.smh.club.api.rest.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.entities.AddressType;
import com.smh.club.api.rest.validation.constraints.PostalCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class AddressBaseDto {

  @SortExclude
  @JsonProperty("member_id")
  private int memberId;

  @NotBlank()
  @JsonProperty("address1")
  private String address1;

  @SortExclude
  @JsonProperty("address2")
  private String address2;

  @NotBlank()
  @JsonProperty("city")
  private String city;

  @NotBlank()
  @JsonProperty("state")
  private String state;

  @PostalCode
  @NotBlank
  @JsonProperty("postal_code")
  private String postalCode;

  @NotNull()
  @JsonProperty("address_type")
  private AddressType addressType;
}
