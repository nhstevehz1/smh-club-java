package com.smh.club.api.hateoas.domain.repos;

import com.smh.club.api.hateoas.domain.entities.EmailEntity;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
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
public class EmailIntegrationTests extends PersistenceTestsBase {

    @Autowired
    private EmailRepo emailRepo;

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
    public void save_email_is_null_throws() {
        // setup
        var email = createEntity(members);
        email.setEmail(null);

        // execute and verify
        assertThrows(Exception.class, () -> emailRepo.save(email));
    }

    @Test
    public void save_emailType_is_null_throws() {
        // setup
        var email = createEntity(members);
        email.setEmailType(null);

        // execute and verify
        assertThrows(Exception.class, () -> emailRepo.save(email));
    }

    @Test
    public void save_member_is_null_throws() {
        // setup
        var email = createEntity(this.members);
        email.setMember(null);

        // execute and verify
        assertThrows(Exception.class, () -> emailRepo.save(email));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_returns_email(int size) {
        // setup
        var email = emailRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));

        // execute
        var ret = emailRepo.findByIdAndMemberId(email.getId(), email.getMember().getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(email, ret.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_unknown_member_id_returns_empty_optional(int size) {
        // setup
        var email = emailRepo.saveAllAndFlush(createList(size, members)).get((int)Math.ceil(size / 2d));
        var ids = this.members.stream()
                .map(MemberEntity::getId).max(Integer::compareTo);
        assertTrue(ids.isPresent());
        var memberId = ids.get() + 100;

        // execute
        var ret = emailRepo.findByIdAndMemberId(email.getId(), memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void findByIdAndMemberId_bad_email_id_returns_empty_optional(int size) {
        // setup
        var emails = emailRepo.saveAllAndFlush(createList(size, members));
        var memberId = emails.get((int)Math.ceil(size / 2d)).getMember().getId();
        var ids = emails.stream()
                .map(EmailEntity::getId).max(Integer::compareTo);

        var emailId = ids.get() + 100;

        // execute
        var ret = emailRepo.findByIdAndMemberId(emailId, memberId);

        //verify
        assertTrue(ret.isEmpty());
    }

    private EmailEntity createEntity(List<MemberEntity> members) {
        return Instancio.of(EmailEntity.class)
                .ignore(field(EmailEntity::getId))
                .generate(field(EmailEntity::getMember), g -> g.oneOf(members))
                .create();
    }

    private List<EmailEntity> createList(int size, List<MemberEntity> members) {
        return Instancio.ofList(EmailEntity.class)
                .size(size)
                .ignore(field(EmailEntity::getId))
                .generate(field(EmailEntity::getMember), g -> g.oneOf(members))
                .create();
    }
}
