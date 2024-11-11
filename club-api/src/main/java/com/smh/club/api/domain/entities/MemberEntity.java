package com.smh.club.api.domain.entities;

import com.smh.club.api.annotations.SortExclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "member_number")
    private int memberNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @SortExclude
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

    @SortExclude
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

    @SortExclude
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

    @SortExclude
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
