package com.smh.club.api.data.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import com.smh.club.api.shared.annotations.SortExclude;

import java.time.LocalDate;

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
    private LocalDate renewalDate;

    @Column(name = "renewal_year", nullable = false)
    private String renewalYear;

    @SortExclude
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;
}
