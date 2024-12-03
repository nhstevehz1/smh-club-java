package com.smh.club.api.data.entities;

import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.domain.EmailType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

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

    @Email
    @Column(name = "email", nullable = false, length = 50)
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
