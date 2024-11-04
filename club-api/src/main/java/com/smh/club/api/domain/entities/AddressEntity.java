package com.smh.club.api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smh.club.api.domain.converters.AddressTypeConverter;
import com.smh.club.api.dto.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "address", schema = "member_mgmt")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true )
    private int id;

    @Column(name = "address1", nullable = false)
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zip", nullable = false)
    private String zip;

    @Builder.Default
    @Column(name = "address_type", nullable = false)
    @Convert(converter = AddressTypeConverter.class)
    private AddressType addressType = AddressType.Home;

    @JsonIgnore // do not serialize to json
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberEntity member;
}
