package com.smh.club.api.rest.domain.repos;

import com.smh.club.api.rest.domain.entities.*;
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

import java.util.Comparator;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.all;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
    public void save_middleName_is_null_does_not_throw() {
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
        assertFalse(addressRepo.existsById(member.getAddresses().getFirst().getId()));
        assertFalse(emailRepo.existsById(member.getEmails().getFirst().getId()));
        assertFalse(phoneRepo.existsById(member.getPhones().getFirst().getId()));
        assertFalse(renewalsRepo.existsById(member.getRenewals().getFirst().getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void delete_children_save_member(int size) {
        // setup
        var member = memberRepo.saveAllAndFlush(createListWithChildren(size)).get((int)Math.ceil(size / 2d));
        var address = member.getAddresses().getFirst();
        var email = member.getEmails().getFirst();
        var phone = member.getPhones().getFirst();
        var renewal = member.getRenewals().getFirst();

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

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    public void should_return_last_used_memberNumber_when_only_one_is_missing(int size) {
        // setup
        var entities = createList(size);
        var idx = 1;
        for(var member : entities) {
            member.setMemberNumber(idx++);
        }
        var expected = entities.get(size/2).getMemberNumber();
        entities.remove(entities.get(size/2));
        memberRepo.saveAllAndFlush(entities);

        // execute
        var actual = memberRepo.findLastUsedMemberNumberBeforeGap();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void should_return_highest_used_memberNumber_when_none_are_missing() {
        // setup
        var entities = createList(10);
        var idx = 1;
        for(var member : entities) {
            member.setMemberNumber(idx++);
        }
        var expected = idx;
        memberRepo.saveAllAndFlush(entities);

        // execute
        var actual = memberRepo.findLastUsedMemberNumberBeforeGap();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void should_return_highest_used_memberNumber_when_first_n_are_missing_missing() {
        // setup
        var entities = createList(10);
        var idx = 1;
        for(var member : entities) {
            member.setMemberNumber(idx++);
        }
        entities.removeFirst();
        entities.removeFirst();
        entities.removeFirst();

        var expected = idx;

        memberRepo.saveAllAndFlush(entities);

        // execute.
        var actual = memberRepo.findLastUsedMemberNumberBeforeGap();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void find_last_used_should_return_null_when_table_is_empty() {
        // execute

        var result = memberRepo.findLastUsedMemberNumberBeforeGap();

        // verify
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    public void should_return_min_memberNumber(int size) {
        // setup
        var entities = createList(size);
        var min = entities.stream().min(Comparator.comparingInt(MemberEntity::getMemberNumber));
        memberRepo.saveAllAndFlush(entities);

        // execute
        var result = memberRepo.findMinMemberNumber();

        // verify
        assertTrue(min.isPresent());
        assertTrue(result.isPresent());
        assertEquals(min.get().getMemberNumber(), result.get());
    }

    @Test
    public void find_min_memberNumber_should_return_null_when_table_is_empty() {
        // execute
        var result = memberRepo.findMinMemberNumber();

        // verify
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    public void should_return_max_memberNumber(int size) {
        // setup
        var entities = createList(size);
        var idx = 1;
        for(var member : entities) {
            member.setMemberNumber(idx++);
        }
        memberRepo.saveAllAndFlush(entities);
        var expected = --idx;

        // execute
        var actual = memberRepo.findMaxMemberNumber();

        // verify
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void find_max_memberNumber_should_return_null_when_table_is_empty() {
        // execute
        var result = memberRepo.findMaxMemberNumber();

        // verify
        assertTrue(result.isEmpty());
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
