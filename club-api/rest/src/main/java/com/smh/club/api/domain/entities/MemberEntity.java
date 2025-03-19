package com.smh.club.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a member object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member", schema = "member_mgmt")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true )
    private int id;

    @Column(name = "member_number", nullable = false, unique = true)
    private int memberNumber;

    @Column(name = "first_name", nullable = false, length = 40)
    private String firstName;

    @Column(name = "middle_name", length = 40)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 40)
    private String lastName;

    @Column(name = "suffix", length = 10)
    private String suffix;

    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @Column(name = "joined_date", nullable = false)
    private Instant joinedDate;

    @Getter
    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<AddressEntity> addresses = new ArrayList<>();

    public void addAddress(AddressEntity address) {
        this.addresses.add(address);
        address.setMember(this);
    }

    public void removeAddress(AddressEntity address) {
        address.setMember(null);
        this.addresses.remove(address);
    }

    @Getter
    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<EmailEntity> emails = new ArrayList<>();

    public void addEmail(EmailEntity email) {
        this.emails.add(email);
        email.setMember(this);
    }

    public void removeEmail(EmailEntity email) {
        email.setMember(null);
        this.emails.remove(email);
    }

    @Getter
    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<PhoneEntity> phones = new ArrayList<>();

    public void addPhone(PhoneEntity phone) {
        this.phones.add(phone);
        phone.setMember(this);
    }

    public void removePhone(PhoneEntity phone) {
        phone.setMember(null);
        this.phones.remove(phone);
    }

    @Getter
    @Builder.Default
    @OneToMany( mappedBy = "member", orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<RenewalEntity> renewals = new ArrayList<>();

    public void addRenewal(RenewalEntity renewal) {
        this.renewals.add(renewal);
        renewal.setMember(this);
    }

    public void removeRenewal(RenewalEntity renewal) {
        renewal.setMember(null);
        this.renewals.remove(renewal);
    }
}
