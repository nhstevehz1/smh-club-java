package com.smh.club.api.data.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import smh.club.shared.api.annotations.SortExclude;
import smh.club.shared.api.domain.EmailType;

/**
 * Represents an email object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "email", schema = "member_mgmt")
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true )
    private int id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Builder.Default
    @Column(name = "email_type", nullable = false)
    private EmailType emailType = EmailType.Home;

    @SortExclude
    @EqualsAndHashCode.Exclude // prevents stack overflow when calling .equals on UserEntity
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;
}
