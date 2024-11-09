package com.smh.club.api.integrationtests.persistence;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.*;
import com.smh.club.api.dto.AddressType;
import com.smh.club.api.dto.EmailType;
import com.smh.club.api.dto.PhoneType;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase (
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests extends PersistenceTestsBase {

    @SuppressWarnings("unused")
    @Autowired
    private MembersRepo membersRepo;

    @SuppressWarnings("unused")
    @Autowired
    private AddressRepo addressRepo;

    @SuppressWarnings("unused")
    @Autowired
    private EmailRepo emailRepo;

    @SuppressWarnings("unused")
    @Autowired
    private PhoneRepo phoneRepo;

    @SuppressWarnings("unused")
    @Autowired
    private RenewalsRepo renewalsRepo;

    @Test
    public void saveMember_Success() {
        // setup
        var joinedDate = LocalDate.now();
        var birthDate = joinedDate.minusYears(30L);
        var m1 = createMember(0, birthDate, joinedDate);

        // execute
        membersRepo.save(m1);
        var id = m1.getId();
        var member = membersRepo.findById(id).orElseThrow();

        // verify
        assertEquals(id, member.getId(), "Id's don't match");
        verifyMember(0, member, birthDate, joinedDate);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20})
    public void saveMembers_Success(int size) {
        // setup
        var joinedDateBase = LocalDate.now();
        var birthDateBase = joinedDateBase.minusYears(30L);
        var members = createMembers(size, birthDateBase, joinedDateBase);

        // execute
        membersRepo.saveAll(members);
        var sortedMembers = membersRepo.findAll(Sort.by("id").ascending());

        // verify
        assertEquals(size, sortedMembers.size(), "Member list size does not match");
        for (int ii = 0; ii < size; ii++) {
            verifyMember(ii, sortedMembers.get(ii), birthDateBase, joinedDateBase);
        }
    }

    @Test
    public void saveMember_MemberNumberNotUniqueThrowsException() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        var m2 = createMember(0, LocalDate.now(), LocalDate.now());
        m2.setMemberNumber(m1.getMemberNumber());

        // Execute and verify
        membersRepo.save(m1);
        assertThrows(Exception.class, () -> membersRepo.save(m2));
    }

    @Test
    public void saveMember_FirstNameIsNullThrowsException() {
        // setup
        var member = createMember(0, LocalDate.now(), LocalDate.now());
        member.setFirstName(null);

        // execute and verify
        assertThrows(Exception.class, () -> membersRepo.save(member));
    }

    @Test
    public void saveMember_LastNameIsNullThrowsException() {
        // setup
        var member = createMember(0, LocalDate.now(), LocalDate.now());
        member.setLastName(null);

        // execute and verify
        assertThrows(Exception.class, () -> membersRepo.save(member));
    }

    @Test
    public void saveMember_MiddleNameIsNullDoesNotThrowsException() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        m1.setMiddleName(null);

        // execute
        var member = membersRepo.save(m1);

        // verify
        assertNull(member.getMiddleName(), "MiddleName is not null");
    }

    @Test
    public void saveMember_SuffixIsNullDoesNotThrowsException() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        m1.setSuffix(null);

        // execute
        var member = membersRepo.save(m1);

        // verify
        assertNull(member.getSuffix(), "Suffix is not null");
    }

    @Test
    public void saveMember_BirthDateIsNullThrowsException() {
        // setup
        var member = createMember(0, LocalDate.now(), LocalDate.now());
        member.setBirthDate(null);

        // execute and verify
        assertThrows(Exception.class, () -> membersRepo.save(member));
    }

    @Test
    public void saveMember_JoinedDateIsNullThrowsException() {
        // setup
        var member = createMember(0, LocalDate.now(), LocalDate.now());
        member.setJoinedDate(null);

        // execute and verify
        assertThrows(Exception.class, () -> membersRepo.save(member));
    }

    @Test
    public void memberNumberExists_ReturnsTrue() {
        // setup
        var localDate = LocalDate.now();
        var members = List.of(
          createMember(1, localDate, localDate),
          createMember(5 ,localDate, localDate),
          createMember(10, localDate, localDate)
        );

        // execute
        var memberList = membersRepo.saveAll(members);

        // verify
        assertEquals(3, memberList.size(), "Member list size doesn't match");
        assertTrue(membersRepo.existsByMemberNumber(1));
        assertTrue(membersRepo.existsByMemberNumber(5));
        assertTrue(membersRepo.existsByMemberNumber(10));
    }

    @Test
    public void memberNumberExists_ReturnsFalse() {
        // setup
        var localDate = LocalDate.now();
        var members = List.of(
                createMember(1, localDate, localDate),
                createMember(5 ,localDate, localDate),
                createMember(10, localDate, localDate)
        );

        // execute
        var memberList = membersRepo.saveAll(members);

        // verify
        assertEquals(3, memberList.size(), "Member list size doesn't match");
        assertFalse(membersRepo.existsByMemberNumber(2));
        assertFalse(membersRepo.existsByMemberNumber(6));
        assertFalse(membersRepo.existsByMemberNumber(11));
    }


    @Test
    public void saveMemberWithChildren() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        m1.addAddress(createAddress(0, AddressType.Work));
        m1.addPhone(createPhone(0, PhoneType.Work));
        m1.addEmail(createEmail(0, EmailType.Work));
        m1.addRenewal(createRenewal(0, LocalDate.now()));

        var id = membersRepo.save(m1).getId();

        // execute
        var member = membersRepo.findById(id).orElseThrow();


        // verify
        assertEquals(1, addressRepo.count());
        assertEquals(1, emailRepo.count());
        assertEquals(1, phoneRepo.count());
        assertEquals(1, renewalsRepo.count());
        assertEquals(1, member.getAddresses().size());
        assertEquals(1, member.getEmails().size());
        assertEquals(1, member.getPhones().size());
        assertEquals(1, member.getRenewals().size());
    }

    @Test
    public void deleteMemberDeletesChildren() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        m1.addAddress(createAddress(0, AddressType.Work));
        m1.addPhone(createPhone(0, PhoneType.Work));
        m1.addEmail(createEmail(0, EmailType.Work));
        m1.addRenewal(createRenewal(0, LocalDate.now()));

        var id = membersRepo.save(m1).getId();

        // execute
        membersRepo.deleteById(id);

        // verify
        assertEquals(0, membersRepo.count());
        assertEquals(0, addressRepo.count());
        assertEquals(0, emailRepo.count());
        assertEquals(0, phoneRepo.count());
        assertEquals(0, renewalsRepo.count());
    }

    @Test
    public void saveMemberDeleteChildren() {
        // setup
        var m1 = createMember(0, LocalDate.now(), LocalDate.now());
        m1.addAddress(createAddress(0, AddressType.Work));
        m1.addEmail(createEmail(0, EmailType.Other));
        m1.addPhone(createPhone(0, PhoneType.Mobile));
        m1.addRenewal(createRenewal(0, LocalDate.now()));
        var id = membersRepo.save(m1).getId();

        // execute
        var member = membersRepo.getReferenceById(id);
        member.removeAddress(member.getAddresses().get(0));
        member.removeEmail(member.getEmails().get(0));
        member.removePhone(member.getPhones().get(0));
        member.removeRenewal(member.getRenewals().get(0));
        membersRepo.save(member);
        var updated = membersRepo.findAll().get(0);

        // verify
        assertEquals(0, updated.getAddresses().size());
        assertEquals(0, updated.getEmails().size());
        assertEquals(0, updated.getPhones().size());
        assertEquals(0, member.getRenewals().size());
        assertEquals(0, addressRepo.count());
        assertEquals(0, emailRepo.count());
        assertEquals(0, phoneRepo.count());
        assertEquals(0, renewalsRepo.count());
    }

    @Test
    public void findMemberByMemberNumber() {
        // setup
        var members = List.of(
                createMember(1, LocalDate.now(), LocalDate.now()),
                createMember(5, LocalDate.now(), LocalDate.now()),
                createMember(10, LocalDate.now(), LocalDate.now())
        );
        membersRepo.saveAll(members);

        // execute
        var member = membersRepo.findByMemberNumber(5);

        // verify
        assertTrue(member.isPresent());
        assertEquals(5, member.get().getMemberNumber());
    }

    @Test
    public void findMemberNumberRange() {
        // setup
        var members = new ArrayList<MemberEntity>();
        for (int ii = 0; ii < 15; ii++ ) {
            if(ii % 3 == 0) {
                members.add(createMember(ii, LocalDate.now(), LocalDate.now()));
            }
        }
        membersRepo.saveAll(members);

        // execute
        var memberNumbers =
                membersRepo.findByMemberNumberGreaterThanEqualAndMemberNumberLessThanEqual(6, 12);

        // verify
        assertEquals(3, memberNumbers.size());
        assertFalse(memberNumbers.stream().anyMatch(mn -> mn.getMemberNumber() == 3));
        assertTrue(memberNumbers.stream().anyMatch(mn -> mn.getMemberNumber() == 6));
        assertTrue(memberNumbers.stream().anyMatch(mn -> mn.getMemberNumber() == 9));
        assertTrue(memberNumbers.stream().anyMatch(mn -> mn.getMemberNumber() == 12));
        assertFalse(memberNumbers.stream().anyMatch(mn -> mn.getMemberNumber() == 15));
    }
}
