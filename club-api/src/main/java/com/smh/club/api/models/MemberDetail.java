package com.smh.club.api.models;

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
public class MemberDetail extends Member{

    @Builder.Default
    @JsonProperty("addresses")
    private List<Address> address = new ArrayList<>();

    @Builder.Default
    @JsonProperty("emails")
    private List<Email> emails = new ArrayList<>();

    @Builder.Default
    @JsonProperty("phones")
    private List<Phone> phones = new ArrayList<>();

    @Builder.Default
    @JsonProperty("renewals")
    private List<Renewal> renewals = new ArrayList<>();
}
