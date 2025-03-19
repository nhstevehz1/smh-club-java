package com.smh.club.api.rest.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Represents a renewal object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "renewal", schema = "member_mgmt")
public class RenewalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true )
    private int id;

    @Column(name = "renewal_date", nullable = false)
    private Instant renewalDate;

    @Column(name = "renewal_year", nullable = false)
    private int renewalYear;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;
}
