package com.smh.club.api.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smh.club.api.data.converters.EmailTypeConverter;
import com.smh.club.api.models.EmailType;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "email", nullable = false)
    private String email;

    @Builder.Default
    @Column(name = "email_type", nullable = false)
    @Convert(converter = EmailTypeConverter.class)
    private EmailType emailType = EmailType.Home;

    @JsonIgnore // Nasty stack overflow when serializing to JSON.
    @EqualsAndHashCode.Exclude // prevents stack overflow when calling .equals on UserEntityr
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;
}
