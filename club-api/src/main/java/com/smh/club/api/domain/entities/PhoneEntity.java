package com.smh.club.api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smh.club.api.domain.converters.PhoneTypeConverter;
import com.smh.club.api.dto.PhoneType;
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

    @Column(name = "phone", nullable = false)
    private String phoneNum;

    @Builder.Default
    @Column(name = "phone_type", nullable = false)
    @Convert(converter = PhoneTypeConverter.class)
    private PhoneType phoneType = PhoneType.Home;

    @JsonIgnore // Nasty stack overflow when serializing to JSON.
    @EqualsAndHashCode.Exclude // prevents stack overflow when calling .equals on MemberEntity
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;

}
