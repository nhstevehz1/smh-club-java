package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.domain.annotations.SortAlias;
import com.smh.club.api.rest.domain.annotations.SortExclude;
import com.smh.club.api.rest.domain.annotations.SortTarget;
import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.domain.entities.AddressType;
import com.smh.club.api.rest.validation.constraints.PostalCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for addresses. Includes member info.
 * Validation annotations exist as a convenience.
 * The intended use of this DTO is with a controller GET method.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(AddressEntity.class)
public class AddressMemberDto {
  @JsonProperty("id")
  private int id;

  @SortAlias("member.memberNumber")
  @JsonProperty("member_number")
  private int memberNumber;

  @SortAlias("member.lastName")
  @JsonProperty("full_name")
  private FullNameDto fullName;

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
