package com.smh.club.api.dto.renewal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.annotations.SortExclude;
import com.smh.club.api.validation.constraints.ValidRenewal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@ValidRenewal
public class RenewalCreateDto {
  @SortExclude
  @JsonProperty("member_id")
  private int memberId;

  @NotNull
  @PastOrPresent
  @JsonProperty("renewal_date")
  private Instant renewalDate;

  @JsonProperty("renewal_year")
  private int renewalYear;
}
