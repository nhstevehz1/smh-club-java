package com.smh.club.api.data.persistence;

import com.smh.club.api.data.repos.AddressRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.AddressType;
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

@SuppressWarnings("unused")
@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
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

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void saveAddress_Success(int addressTypeInt) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var addressType = AddressType.getAddressType(addressTypeInt);
        var a1 = createAddress(0, addressType);
        a1.setMember(member);

        // execute
        var address = addressRepo.save(a1);

        // verify
        verifyAddress(0, address, addressType, member.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10, 15, 20})
    public void saveAddresses_Success(int size) {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var addressList = createAddresses(size);
        for (var address : addressList) {
            address.setMember(member);
        }

        // execute
        addressRepo.saveAll(addressList);
        var sortedAddresses = addressRepo.findAll(Sort.by("id").ascending());

        // verify
        assertEquals(size, sortedAddresses.size(), "Address list size doesn't match");
        for (int ii = 0; ii < size; ii++) {
            verifyAddress(ii, sortedAddresses.get(ii), AddressType.Home, member.getId());
        }
    }

    @Test
    public void saveAddress_Address1IsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Work);
        address.setMember(member);
        address.setAddress1(null);

        //execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void saveAddress_CityIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Home);
        address.setMember(member);
        address.setCity(null);

        //execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void saveAddress_StateIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Home);
        address.setMember(member);
        address.setState(null);

        //execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void saveAddress_ZipIsNullThrowsException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Home);
        address.setMember(member);
        address.setZip(null);

        //execute and verify
        assertThrows(Exception.class, () -> addressRepo.save(address));
    }

    @Test
    public void saveAddress_Address2IsNullDoesNotThrowException() {
        // setup
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Home);
        address.setMember(member);
        address.setAddress2(null);

        // execute
        var ret = addressRepo.save(address);

        // verify
        assertNull(address.getAddress2(), "Address2 is not null");
    }

    @Test
    public void findByIdAndMemberId_returns_address() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));
        var address = createAddress(0, AddressType.Home);
        address.setMember(member);
        var entity = addressRepo.save(address);

        // execute
        var ret = addressRepo.findByIdAndMemberId(address.getId(), member.getId());

        //verify
        assertTrue(ret.isPresent());
        assertEquals(member.getId(), ret.get().getMember().getId());
        assertEquals(entity.getId(), ret.get().getId());
    }

    @Test
    public void findByIdAndMemberId_returns_empty_optional() {
        var member = membersRepo.save(createMember(0, LocalDate.now(), LocalDate.now()));

        var entity = createAddress(0, AddressType.Home);
        entity.setMember(member);

        var saved = addressRepo.save(entity);

        // execute
        var ret = addressRepo.findByIdAndMemberId(saved.getId(),10);

        //verify
        assertFalse(ret.isPresent());
    }
}
