package com.smh.club.api.rest.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.rest.dto.address.AddressDto;
import com.smh.club.api.rest.dto.email.EmailDto;
import com.smh.club.api.rest.dto.phone.PhoneDto;
import com.smh.club.api.rest.dto.renewal.RenewalDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MemberDetailDto extends MemberBaseDto {

    @JsonProperty("id")
    private int id;

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
