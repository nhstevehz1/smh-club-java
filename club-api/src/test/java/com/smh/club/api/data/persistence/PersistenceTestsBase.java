package com.smh.club.api.data.persistence;

import com.smh.club.api.data.entities.*;
import com.smh.club.api.models.AddressType;
import com.smh.club.api.models.EmailType;
import com.smh.club.api.models.PhoneType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class PersistenceTestsBase {

    protected MemberEntity createMember(int flag, LocalDate birthDate, LocalDate joinedDate) {
        return MemberEntity.builder()
                .memberNumber(flag)
                .birthDate(birthDate)
                .joinedDate(joinedDate)
                .firstName("First_" + flag)
                .middleName("Middle_" + flag)
                .lastName("Last_" + flag)
                .suffix("Suffix_" + flag)
                .build();
    }

    protected List<MemberEntity> createMembers(int size, LocalDate birthDate, LocalDate joinedDate) {
        List<MemberEntity> members = new ArrayList<>();

        for (int ii = 0; ii < size; ii++) {
            members.add(createMember(ii, birthDate.minusYears(ii), joinedDate.minusYears(ii)));
        }

        return members;
    }

    protected void verifyMember(int flag, MemberEntity member, LocalDate birthDate, LocalDate joinedDate) {
        assertEquals(flag, member.getMemberNumber(), "memberNumber doesn't match");
        assertEquals("First_" + flag, member.getFirstName(), "firstName doesn't match");
        assertEquals("Middle_" + flag, member.getMiddleName(), "middleName doesn't match");
        assertEquals("Last_" + flag, member.getLastName(), "LastName doesn't match");
        assertEquals("Suffix_" + flag, member.getSuffix(), "Suffix doesn't match");
        assertEquals(birthDate.minusYears(flag), member.getBirthDate(), "birthDate doesn't match");
        assertEquals(joinedDate.minusYears(flag), member.getJoinedDate(), "joinedYear doesn't match");

        member.getAddresses()
                .forEach(a -> assertEquals(member.getId(), a.getMember().getId(), "Address Member id doesn't match"));

        member.getEmails()
                .forEach((e -> assertEquals(member.getId(), e.getMember().getId(), "Email member id doesn't match")));

        member.getPhones()
                .forEach((p -> assertEquals(member.getId(), p.getMember().getId(), "Phone member id doesn't match")));
    }

    protected AddressEntity createAddress(int flag, AddressType addressType) {
        return AddressEntity.builder()
                .address1("Address1_" + flag)
                .address2("Address2_" + flag)
                .city("City_" + flag)
                .state("ST_" + flag)
                .zip("Zip_" + flag)
                .addressType(addressType)
                .build();
    }

    protected List<AddressEntity> createAddresses(int size) {
        List<AddressEntity> addressList = new ArrayList<>();

        for (int ii= 0; ii < size; ii++) {
            addressList.add(createAddress(ii, AddressType.Home));
        }

        return addressList;
    }

    protected void verifyAddress(int flag, AddressEntity address, AddressType addressType, int memberId) {
        assertNotNull(address.getMember(), "MemberEntity is null");
        assertEquals(memberId, address.getMember().getId(), "Member id doesn't match" );
        assertEquals("Address1_" + flag, address.getAddress1(), "Address1 doesn't match");
        assertEquals("Address2_" + flag, address.getAddress2(), "Address2 doesn't match");
        assertEquals("City_" + flag, address.getCity(), "City doesn't match");
        assertEquals("ST_" + flag, address.getState(), "State doesn't match");
        assertEquals("Zip_" + flag, address.getZip(), "Zip doesn't match");
        assertEquals(addressType, address.getAddressType(), "AddressType doesn't match");
    }

    protected EmailEntity createEmail(int flag, EmailType emailType) {
        return EmailEntity.builder()
                .email("Email_" + flag + "@email.com")
                .emailType(emailType)
                .build();
    }

    protected List<EmailEntity> createEmails(int size) {
        List<EmailEntity> emailList = new ArrayList<>();

        for (int ii= 0; ii < size; ii++) {
            emailList.add(createEmail(ii, EmailType.Home));
        }

        return emailList;
    }

    protected void verifyEmail(int flag, EmailEntity email, EmailType emailType, int memberId) {
        assertNotNull(email.getMember());
        assertEquals(memberId, email.getMember().getId(), "Member id doesn't match");
        assertEquals("Email_" + flag + "@email.com", email.getEmail(), "Email address doesn't match");
        assertEquals(emailType, email.getEmailType(), "EmailType doesn't match");
    }

    protected PhoneEntity createPhone(int flag, PhoneType phoneType) {
        return PhoneEntity.builder()
                .phoneNum("Phone_" + flag)
                .phoneType(phoneType)
                .build();
    }

    protected List<PhoneEntity> createPhones(int size) {
        List<PhoneEntity> phoneList = new ArrayList<>();

        for (int ii= 0; ii < size; ii++) {
            phoneList.add(createPhone(ii, PhoneType.Home));
        }

        return phoneList;
    }

    protected void verifyPhone(int flag, PhoneEntity phone, PhoneType phoneType, int memberId) {
        assertNotNull(phone.getMember(), "Member is null");
        assertEquals(memberId, phone.getMember().getId(), "Member id doesn't match");
        assertEquals("Phone_" + flag , phone.getPhoneNum(), "PhoneNumber doesn't match");
        assertEquals(phoneType, phone.getPhoneType(), "PhoneType doesn't match");
    }

    protected RenewalEntity createRenewal(int flag, LocalDate renewalDate) {
        return RenewalEntity.builder()
                .renewalDate(renewalDate.minusYears(flag))
                .renewalYear(Integer.toString(renewalDate.plusYears(1).getYear()))
                .build();
    }

    protected List<RenewalEntity> createRenewals(int size, LocalDate renewalDate){
        List<RenewalEntity> renewalList = new ArrayList<>();

        for (int ii= 0; ii < size; ii++) {
            renewalList.add(createRenewal(ii, renewalDate));
        }

        return renewalList;
    }

    protected void verifyRenewal(int flag, RenewalEntity renewal, LocalDate renewalDate, int memberId) {
        assertNotNull(renewal.getMember(), "Member is null");
        assertEquals(memberId, renewal.getMember().getId(), "Member id doesn't match");
        assertEquals(renewalDate.minusYears(flag), renewal.getRenewalDate(), "Renewal date does not match");
        var renewalYear = Integer.toString(renewalDate.plusYears(1).getYear());
        assertEquals(renewalYear, renewal.getRenewalYear(), "Renewal year doesn't match");
    }
}
