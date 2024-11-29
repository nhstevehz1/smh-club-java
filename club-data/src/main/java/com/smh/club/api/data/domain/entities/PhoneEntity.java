package com.smh.club.api.data.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import smh.club.shared.api.annotations.SortExclude;
import smh.club.shared.api.domain.PhoneType;

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

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Builder.Default
    @Column(name = "phone_type", nullable = false)
    private PhoneType phoneType = PhoneType.Home;

    @SortExclude
    @EqualsAndHashCode.Exclude // prevents stack overflow when calling .equals on MemberEntity
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;

}
