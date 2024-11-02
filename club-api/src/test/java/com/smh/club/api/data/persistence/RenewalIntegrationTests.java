package com.smh.club.api.data.persistence;

import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
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
public class RenewalIntegrationTests extends PersistenceTestsBase {

    @SuppressWarnings("unused")
    @Autowired
    private RenewalsRepo renewalsRepo;

    @SuppressWarnings("unused")
    @Autowired
    private MembersRepo membersRepo;

    @Test
    public void saveRenewal_Success() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var localDate = LocalDate.now();
        var r1 = createRenewal(0, localDate);
        r1.setMember(member);

        // execute
        var renewal = this.renewalsRepo.save(r1);

        // verify
        verifyRenewal(0, renewal, localDate, member.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10, 15, 20})
    public void saveRenewals_Success(int size) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var localDate = LocalDate.now();
        var renewalList = createRenewals(size, localDate);
        for (var renewal : renewalList) {
            renewal.setMember(member);
        }

        // execute
        renewalsRepo.saveAll(renewalList);
        var sortedRenewals = renewalsRepo.findAll(Sort.by("id").ascending());

        // verify
        assertEquals(size, sortedRenewals.size(), "Renewal size doesn't match");
        for (int ii = 0; ii < size; ii++) {
            verifyRenewal(ii, sortedRenewals.get(ii), localDate, member.getId());
        }
    }

    @Test
    public void saveRenewal_RenewalDateIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var renewal = createRenewal(0,LocalDate.now());
        renewal.setMember(member);
        renewal.setRenewalDate(null);

        //execute and verify
        assertThrows(Exception.class, () -> renewalsRepo.save(renewal));
    }

    @Test
    public void saveRenewal_RenewalYearIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var renewal = createRenewal(0, LocalDate.now());
        renewal.setRenewalYear(null);
        renewal.setMember(member);

        // execute and verify
        assertThrows(Exception.class, () -> renewalsRepo.save(renewal));
    }

    @Test
    public void saveRenewal_MemberIsNullThrowsException() {
        // setup
        var renewal = createRenewal(0, LocalDate.now());

        // execute and verify
        assertThrows(Exception.class, () -> renewalsRepo.save(renewal));
    }

    @Test
    public void findByIdAndMemberId_returns_renewal() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));

        var entity = createRenewal(0, LocalDate.now());
        entity.setMember(member);

        var saved = renewalsRepo.save(entity);

        // execute
        var ret = renewalsRepo.findByIdAndMemberId(saved.getId(), member.getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(saved.getId(), ret.get().getId());
        assertEquals(member.getId(), ret.get().getMember().getId());
    }

    @Test
    public void findByIdAndMemberId_returns_empty_optional() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var entity = createRenewal(0, LocalDate.now());
        entity.setMember(member);

        var saved = renewalsRepo.save(entity);

        // execute
        var ret = renewalsRepo.findByIdAndMemberId(saved.getId(),10);

        //verify
        assertFalse(ret.isPresent());
    }
}
