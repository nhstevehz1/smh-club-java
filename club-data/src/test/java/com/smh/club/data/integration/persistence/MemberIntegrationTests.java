package com.smh.club.data.integration.persistence;

import com.smh.club.data.domain.entities.*;
import com.smh.club.data.domain.repos.*;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.all;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@ExtendWith(InstancioExtension.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase (
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests {

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private EmailRepo emailRepo;

    @Autowired
    private PhoneRepo phoneRepo;

    @Autowired
    private RenewalsRepo renewalsRepo;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void save_memberNumber_not_unique_throws(int size) {
        // setup
        var members = createList(size);
        var number = memberRepo.saveAll(members).get((int)Math.ceil(size / 2d)).getMemberNumber();
        var member = createEntity();
        member.setMemberNumber(number);

        // execute and verify
        assertThrows(Exception.class, () -> memberRepo.save(member));
    }

    @Test
    public void save_firstName_is_null_throws() {
        // setup
        var member = createEntity();
        member.setFirstName(null);

        // execute and verify
        assertThrows(Exception.class, () -> memberRepo.save(member));
    }

    @Test
    public void save_middleName_is_null_does_not_throws() {
        // setup
        var member = createEntity();
        member.setMiddleName(null);

        // execute and verify
        memberRepo.save(member);
    }

    @Test
    public void save_lastName_is_null_throws() {
        // setup
        var member = createEntity();
        member.setLastName(null);

        // execute and verify
        assertThrows(Exception.class, () -> memberRepo.save(member));
    }

    @Test
    public void save_suffix_is_null_does_not_throws() {
        // setup
        var member = createEntity();
        member.setSuffix(null);

        // execute and verify
        memberRepo.save(member);
    }

    @Test
    public void save_birthDate_is_null_throws() {
        // setup
        var member = createEntity();
        member.setBirthDate(null);

        // execute and verify
        assertThrows(Exception.class, () -> memberRepo.save(member));
    }

    @Test
    public void save_joinedDate_is_null_throws() {
        // setup
        var member = createEntity();
        member.setJoinedDate(null);

        // execute and verify
        assertThrows(Exception.class, () -> memberRepo.save(member));
    }

    @ParameterizedTest
    @ValueSource(ints = {5,20,50})
    public void get_memberNumbers_matches(int size) {
        // setup
        var members = memberRepo.saveAll(createList(size));
        var numbers = members.stream().map(MemberEntity::getMemberNumber).toList();
        var highest = members.stream().map(MemberEntity::getMemberNumber).max(Integer::compareTo).orElseThrow();

        // execute
        var ret = memberRepo.findByMemberNumberGreaterThanEqualAndMemberNumberLessThanEqual(0, highest);
        var vals = ret.stream().map(MemberNumberOnly::getMemberNumber).toList();

        // assert
        vals.forEach(v -> assertTrue(numbers.contains(v)));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void save_with_children_success(int size) {
        // setup
        var members = memberRepo.saveAll(createListWithChildren(size));

        // execute
        var ret = memberRepo.findAll();

        //verify
        assertEquals(members, ret);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void delete_member_deletes_children(int size) {
        // setup
        var member = memberRepo.saveAllAndFlush(createListWithChildren(size)).get((int)Math.ceil(size / 2d));

        // execute
        memberRepo.deleteById(member.getId());

        // verify
        assertFalse(memberRepo.existsById(member.getId()));
        assertFalse(addressRepo.existsById(member.getAddresses().get(0).getId()));
        assertFalse(emailRepo.existsById(member.getEmails().get(0).getId()));
        assertFalse(phoneRepo.existsById(member.getPhones().get(0).getId()));
        assertFalse(renewalsRepo.existsById(member.getRenewals().get(0).getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void delete_children_save_member(int size) {
        // setup
        var member = memberRepo.saveAllAndFlush(createListWithChildren(size)).get((int)Math.ceil(size / 2d));
        var address = member.getAddresses().get(0);
        var email = member.getEmails().get(0);
        var phone = member.getPhones().get(0);
        var renewal = member.getRenewals().get(0);

        member.removeAddress(address);
        member.removeEmail(email);
        member.removePhone(phone);
        member.removeRenewal(renewal);

        // execute
        var ret = memberRepo.save(member);

        // verify
        assertTrue(ret.getAddresses().isEmpty());
        assertTrue(ret.getEmails().isEmpty());
        assertTrue(ret.getPhones().isEmpty());
        assertTrue(ret.getRenewals().isEmpty());
        assertFalse(addressRepo.existsById(address.getId()));
        assertFalse(emailRepo.existsById(email.getId()));
        assertFalse(phoneRepo.existsById(phone.getId()));
        assertFalse(renewalsRepo.existsById(renewal.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findMemberByMemberNumber(int size) {
        // setup
        var member = memberRepo.saveAllAndFlush(createListWithChildren(size)).get((int)Math.ceil(size / 2d));

        // execute
        var ret = memberRepo.findByMemberNumber(member.getMemberNumber());

        // verify
        assertTrue(ret.isPresent());
        assertEquals(member, ret.get());
    }

    private MemberEntity createEntity() {
        return Instancio.of(MemberEntity.class)
                .ignore(field(MemberEntity::getId))
                .create();
    }

    private List<MemberEntity> createList(int size) {
        return Instancio.ofList(MemberEntity.class)
                .size(size)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
    }

    private List<MemberEntity> createListWithChildren(int size) {
        return Instancio.ofList(MemberEntity.class)
                .size(size)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .onComplete(all(MemberEntity.class), (MemberEntity member) -> {
                    member.addAddress(createAddress());
                    member.addEmail(createEmail());
                    member.addPhone(createPhone());
                    member.addRenewal(createRenewal());
                })
                .create();
    }

    private AddressEntity createAddress() {
        return Instancio.of(AddressEntity.class)
                .ignore(field(AddressEntity::getId))
                .ignore(field(AddressEntity::getMember))
                .create();
    }

    private EmailEntity createEmail() {
        return Instancio.of(EmailEntity.class)
                .ignore(field(EmailEntity::getId))
                .ignore(field(EmailEntity::getMember))
                .create();
    }

    private PhoneEntity createPhone() {
        return Instancio.of(PhoneEntity.class)
                .ignore(field(PhoneEntity::getId))
                .ignore(field(PhoneEntity::getMember))
                .create();
    }

    private RenewalEntity createRenewal() {
        return Instancio.of(RenewalEntity.class)
                .ignore(field(RenewalEntity::getId))
                .ignore(field(RenewalEntity::getMember))
                .create();
    }

 }
