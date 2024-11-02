package com.smh.club.api.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder // listed as experimental on lombok site
public class MemberDetailDto extends MemberDto {

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
