package com.smh.club.api.data.domain.entities;

import com.smh.club.api.data.annotations.SortExclude;
import com.smh.club.api.data.dto.AddressType;
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

    @Column(name = "address1", nullable = false, length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "state", nullable = false, length = 30)
    private String state;

    @Column(name = "zip", nullable = false, length = 15)
    private String zip;

    @Builder.Default
    @Column(name = "address_type", nullable = false)
    private AddressType addressType = AddressType.Home;

    @SortExclude
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberEntity member;
}
