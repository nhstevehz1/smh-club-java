package com.smh.club.data.domain.entities;

import com.smh.club.data.annotations.SortExclude;
import com.smh.club.data.dto.PhoneType;
import jakarta.persistence.*;
import lombok.*;

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
