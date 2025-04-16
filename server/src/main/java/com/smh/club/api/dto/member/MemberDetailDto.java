package com.smh.club.api.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MemberDetailDto extends MemberDto {

    @JsonProperty("id")
    private int id;

    @Builder.Default
    @JsonProperty("addresses")
    private final List<AddressDto> addresses = new ArrayList<>();

    @Builder.Default
    @JsonProperty("emails")
    private final List<EmailDto> emails = new ArrayList<>();

    @Builder.Default
    @JsonProperty("phones")
    private final List<PhoneDto> phones = new ArrayList<>();

    @Builder.Default
    @JsonProperty("renewals")
    private final List<RenewalDto> renewals = new ArrayList<>();
}
