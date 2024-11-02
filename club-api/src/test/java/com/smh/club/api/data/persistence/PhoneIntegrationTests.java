package com.smh.club.api.data.persistence;

import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.PhoneRepo;
import com.smh.club.api.data.dto.PhoneType;
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

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase (
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_CLASS)
public class PhoneIntegrationTests extends PersistenceTestsBase {

    @SuppressWarnings("unused")
    @Autowired
    private PhoneRepo phoneRepo;

    @SuppressWarnings("unused")
    @Autowired
    private MembersRepo membersRepo;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void savePhone_Success(int phoneTypeInt) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var phoneType = PhoneType.getPhoneType(phoneTypeInt);
        var e1 = createPhone(0, phoneType);
        e1.setMember(member);

        // execute
        var phone = this.phoneRepo.save(e1);

        // verify
        verifyPhone(0, phone, phoneType, member.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10, 15, 20})
    public void savePhones_Success(int size) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var phoneList = createPhones(size);
        for (var phone : phoneList) {
            phone.setMember(member);
        }

        // execute
        phoneRepo.saveAll(phoneList);
        var sortedPhones = phoneRepo.findAll(Sort.by("id").ascending());

        // verify
        assertEquals(size, sortedPhones.size(), "Phone list size does not match");
        for (int ii = 0; ii < size; ii++) {
            verifyPhone(ii, sortedPhones.get(ii), PhoneType.Home, member.getId());
        }
    }

    @Test
    public void savePhone_PhoneNumberIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var phone = createPhone(0, PhoneType.Work);
        phone.setMember(member);
        phone.setPhoneNum(null);

        //execute and verify
        assertThrows(Exception.class, () -> phoneRepo.save(phone));
    }

    @Test
    public void savePhone_MemberIsNullThrowsException() {
        // setup
        var phone = createPhone(0, PhoneType.Home);

        // execute and verify
        assertThrows(Exception.class, () -> phoneRepo.save(phone));
    }

    @Test
    public void findByIdAndMemberId_returns_email() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));

        var entity = createPhone(0, PhoneType.Other);
        entity.setMember(member);

        var saved = phoneRepo.save(entity);

        // execute
        var ret = phoneRepo.findByIdAndMemberId(saved.getId(), member.getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(saved.getId(), ret.get().getId());
        assertEquals(member.getId(), ret.get().getMember().getId());
    }

    @Test
    public void findByIdAndMemberId_returns_empty_optional() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var entity = createPhone(0, PhoneType.Other);
        entity.setMember(member);

        var saved = phoneRepo.save(entity);

        // execute
        var ret = phoneRepo.findByIdAndMemberId(saved.getId(),10);

        //verify
        assertFalse(ret.isPresent());
    }

}
