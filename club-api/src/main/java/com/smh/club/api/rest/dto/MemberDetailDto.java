package com.smh.club.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    @JsonProperty("id")
    private int id;

    @Min(1)
    @JsonProperty("member_number")
    private int memberNumber;

    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @NotBlank
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("suffix")
    private String suffix;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("joined_date")
    private LocalDate joinedDate;

    @Builder.Default
    @JsonProperty("addresses")
    private List<AddressDto> addresses = new ArrayList<>();

    @Builder.Default
    @JsonProperty("emails")
    private List<EmailDto> emails = new ArrayList<>();

    @Builder.Default
    @JsonProperty("phones")
    private List<PhoneDto> phones = new ArrayList<>();

    @Builder.Default
    @JsonProperty("renewals")
    private List<RenewalDto> renewals = new ArrayList<>();
}
