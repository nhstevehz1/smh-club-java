package com.smh.club.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an Address object.
 */
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

    @Column(name = "address1", nullable = false, length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "state", nullable = false, length = 30)
    private String state;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Builder.Default
    @Column(name = "address_type", nullable = false)
    private AddressType addressType = AddressType.Home;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberEntity member;
}
