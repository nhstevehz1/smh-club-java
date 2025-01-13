package com.smh.club.api.rest.domain.entities;

import com.smh.club.api.rest.domain.annotations.SortExclude;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a phone object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "phone", schema = "member_mgmt")
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true )
    private int id;

    @Column(name="country_code", nullable = false, length = 5)
    private String countryCode;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Builder.Default
    @Column(name = "phone_type", nullable = false)
    private PhoneType phoneType = PhoneType.Home;

    @SortExclude
    @EqualsAndHashCode.Exclude // prevents stack overflow when calling .equals on MemberEntity
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;

}
