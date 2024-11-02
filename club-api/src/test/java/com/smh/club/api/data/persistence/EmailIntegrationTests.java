package com.smh.club.api.data.persistence;

import com.smh.club.api.data.repos.EmailRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.dto.EmailType;
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
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class EmailIntegrationTests extends PersistenceTestsBase {

    @SuppressWarnings("unused")
    @Autowired
    private EmailRepo emailRepo;

    @SuppressWarnings("unused")
    @Autowired
    private MembersRepo membersRepo;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void saveEmail_Success(int emailTypeInt) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var emailType = EmailType.getEmailType(emailTypeInt);
        var e1 = createEmail(0, emailType);
        e1.setMember(member);

        // execute
        var email = this.emailRepo.save(e1);

        // verify
        verifyEmail(0, email, emailType, member.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10, 15, 20})
    public void saveEmails_Success(int size) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var emailList = createEmails(size);
        for (var email : emailList) {
            email.setMember(member);
        }

        // execute
        emailRepo.saveAll(emailList);
        var sortedEmails = emailRepo.findAll(Sort.by("id").ascending());

        // verify
        assertEquals(size, sortedEmails.size(), "Email list size doesn't match");
        for (int ii = 0; ii < size; ii++) {
            verifyEmail(ii, sortedEmails.get(ii), EmailType.Home, member.getId());
        }
    }

    @Test
    public void saveEmail_EmailIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var email = createEmail(0, EmailType.Work);
        email.setMember(member);
        email.setEmail(null);

        //execute and verify
        assertThrows(Exception.class, () -> emailRepo.save(email));
    }

    @Test
    public void findByIdAndMemberId_returns_email() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));

        var entity = createEmail(0, EmailType.Other);
        entity.setMember(member);

        var saved = emailRepo.save(entity);

        // execute
        var ret = emailRepo.findByIdAndMemberId(saved.getId(), member.getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(saved.getId(), ret.get().getId());
        assertEquals(member.getId(), ret.get().getMember().getId());
    }

    @Test
    public void findByIdAndMemberId_returns_empty_optional() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var entity = createEmail(0, EmailType.Home);
        entity.setMember(member);

        var saved = emailRepo.save(entity);

        // execute
        var ret = emailRepo.findByIdAndMemberId(saved.getId(),10);

        //verify
        assertFalse(ret.isPresent());
    }

}
