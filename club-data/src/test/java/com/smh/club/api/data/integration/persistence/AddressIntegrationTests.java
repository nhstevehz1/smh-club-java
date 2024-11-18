package com.smh.club.api.data.integration.persistence;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
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
import static org.instancio.Select.field;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@ExtendWith(InstancioExtension.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
class AddressIntegrationTests extends PersistenceTestsBase {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private MembersRepo membersRepo;

    private List<MemberEntity> members;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);


    @BeforeEach
    public void init() {
        var list = createMembers(5);
        this.members = membersRepo.saveAllAndFlush(list);
    }

    @Test
    public void save_address1_null_throws() {
        // setup
        var address = createEntity(this.members);
        address.setAddress1(null);

        // execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void save_address2_not_null_does_not_throws() {
        // setup
        var address = createEntity(this.members);
        address.setAddress2(null);

        // execute and verify
        addressRepo.save(address); // should not throw.
    }

    @Test
    public void save_city_is_null_throws() {
        // setup
        var address = createEntity(this.members);
        address.setCity(null);

        // execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void save_member_is_null_throws() {
        // setup
        var address = createEntity(this.members);
        address.setMember(null);

        // execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void save_state_is_null_throws() {
        // setup
        var address = createEntity(this.members);
        address.setState(null);

        // execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void save_zip_is_null_throws() {
        // setup
        var address = createEntity(this.members);
        address.setZip(null);

        // execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_returns_address(int size) {
        // setup
        var address = addressRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));

        // execute
        var ret = addressRepo.findByIdAndMemberId(address.getId(), address.getMember().getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(address, ret.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_unknown_member_id_returns_empty_optional(int size) {
        // setup
        var address = addressRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));
        var ids = this.members.stream()
                .map(MemberEntity::getId).max(Integer::compareTo);
        assertTrue(ids.isPresent());
        var memberId = ids.get() + 100;

        // execute
        var ret = addressRepo.findByIdAndMemberId(address.getId(), memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_bad_address_id_returns_empty_optional(int size) {
        // setup
        var addresses = addressRepo.saveAllAndFlush(createList(size, members));
        var memberId = addresses.get((int)Math.ceil(size / 2d)).getMember().getId();
        var ids = addresses.stream()
                .map(AddressEntity::getId).max(Integer::compareTo);

        var addressId = ids.get() + 100;

        // execute
        var ret = addressRepo.findByIdAndMemberId(addressId, memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    private AddressEntity createEntity(List<MemberEntity> members) {
        return Instancio.of(AddressEntity.class)
                .ignore(field(AddressEntity::getId))
                .generate(field(AddressEntity::getMember), g -> g.oneOf(members) )
                .create();
    }

    private List<AddressEntity> createList(int size, List<MemberEntity> members) {
        return Instancio.ofList(AddressEntity.class)
                .size(size)
                .ignore(field(AddressEntity::getId))
                .generate(field(AddressEntity::getMember), g -> g.oneOf(members) )
                .create();
    }
}
