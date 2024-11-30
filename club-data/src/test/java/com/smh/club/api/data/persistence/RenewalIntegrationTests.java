package com.smh.club.api.data.persistence;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
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
import static org.junit.Assert.assertEquals;
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
public class RenewalIntegrationTests extends PersistenceTestsBase {

    @Autowired
    private RenewalsRepo renewalRepo;
    
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
    public void save_renewalDate_is_null_throws() {
        // setup
        var renewal = createEntity(members);
        renewal.setRenewalDate(null);

        // execute and verify
        assertThrows(Exception.class, () -> renewalRepo.save(renewal));
    }

    @Test
    public void save_renewalYear_is_null_throws() {
        // setup
        var renewal = createEntity(members);
        renewal.setRenewalYear(null);

        // execute and verify
        assertThrows(Exception.class, () -> renewalRepo.save(renewal));
    }

    @Test
    public void save_member_is_null_throws() {
        // setup
        var renewal = createEntity(members);
        renewal.setMember(null);

        // execute and verify
        assertThrows(Exception.class, () -> renewalRepo.save(renewal));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_returns_renewal(int size) {
        // setup
        var renewal = renewalRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));

        // execute
        var ret = renewalRepo.findByIdAndMemberId(renewal.getId(), renewal.getMember().getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(renewal, ret.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_unknown_member_id_returns_empty_optional(int size) {
        // setup
        var renewal = renewalRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));
        var ids = this.members.stream()
                .map(MemberEntity::getId).max(Integer::compareTo);
        assertTrue(ids.isPresent());
        var memberId = ids.get() + 100;

        // execute
        var ret = renewalRepo.findByIdAndMemberId(renewal.getId(), memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_bad_renewal_id_returns_empty_optional(int size) {
        // setup
        var renewals = renewalRepo.saveAllAndFlush(createList(size, members));
        var memberId = renewals.get((int)Math.ceil(size / 2d)).getMember().getId();
        var ids = renewals.stream()
                .map(RenewalEntity::getId).max(Integer::compareTo);

        var renewalId = ids.get() + 100;

        // execute
        var ret = renewalRepo.findByIdAndMemberId(renewalId, memberId);

        //verify
        assertTrue(ret.isEmpty());
    }
    
    private RenewalEntity createEntity(List<MemberEntity> members) {
        return Instancio.of(RenewalEntity.class)
                .ignore(field(RenewalEntity::getId))
                .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
                .create();
    }

    private List<RenewalEntity> createList(int size, List<MemberEntity> members) {
        return Instancio.ofList(RenewalEntity.class)
                .size(size)
                .ignore(field(RenewalEntity::getId))
                .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
                .create();
    }
}
