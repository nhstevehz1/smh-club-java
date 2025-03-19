package com.smh.club.api.domain.repos;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.List;
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

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class PhoneIntegrationTests extends PersistenceTestsBase {

   @Autowired
   private PhoneRepo phoneRepo;

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
        var list = createMembers();
        this.members = membersRepo.saveAllAndFlush(list);
    }
    
    @Test
    public void save_phoneNumber_is_null_throws() {
        // setup
        var phone = createEntity(members);
        phone.setPhoneNumber(null);
        
        // execute and verify
        assertThrows(Exception.class, () -> phoneRepo.save(phone));
    }

    @Test
    public void save_phoneType_is_null_throws() {
        // setup
        var phone = createEntity(members);
        phone.setPhoneType(null);

        // execute and verify
        assertThrows(Exception.class, () -> phoneRepo.save(phone));
    }

    @Test
    public void save_member_is_null_throws() {
        // setup
        var phone = createEntity(members);
        phone.setMember(null);

        // execute and verify
        assertThrows(Exception.class, () -> phoneRepo.save(phone));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_returns_phone(int size) {
        // setup
        var phone = phoneRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));

        // execute
        var ret = phoneRepo.findByIdAndMemberId(phone.getId(), phone.getMember().getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(phone, ret.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_unknown_member_id_returns_empty_optional(int size) {
        // setup
        var phone = phoneRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));
        var ids = this.members.stream()
                .map(MemberEntity::getId).max(Integer::compareTo);
        assertTrue(ids.isPresent());
        var memberId = ids.get() + 100;

        // execute
        var ret = phoneRepo.findByIdAndMemberId(phone.getId(), memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_bad_phone_id_returns_empty_optional(int size) {
        // setup
        var phones = phoneRepo.saveAllAndFlush(createList(size, members));
        var memberId = phones.get((int)Math.ceil(size / 2d)).getMember().getId();
        var ids = phones.stream()
                .map(PhoneEntity::getId).max(Integer::compareTo);

        var phoneId = ids.get() + 100;

        // execute
        var ret = phoneRepo.findByIdAndMemberId(phoneId, memberId);

        //verify
        assertTrue(ret.isEmpty());
    }
    
    private PhoneEntity createEntity(List<MemberEntity> members) {
        return Instancio.of(PhoneEntity.class)
                .ignore(field(PhoneEntity::getId))
                .generate(field(PhoneEntity::getMember), g -> g.oneOf(members))
                .create();
    }

    private List<PhoneEntity> createList(int size, List<MemberEntity> members) {
        return Instancio.ofList(PhoneEntity.class)
                .size(size)
                .ignore(field(PhoneEntity::getId))
                .generate(field(PhoneEntity::getMember), g -> g.oneOf(members))
                .create();
    }
}
