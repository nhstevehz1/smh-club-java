package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.config.MapperConfig;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.rest.dto.MemberDto;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InstancioExtension.class)
public class MemberMapperTests {

    private final MemberMapperImpl mapper
            = new MemberMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);
            //.set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_create_to_entity() {
        // setup
        var member = Instancio.create(MemberDto.class);

        // execute
        var entity = mapper.toEntity(member);

        // verify
        assertEquals(0, entity.getId());
        assertEquals(member.getMemberNumber(), entity.getMemberNumber());
        assertEquals(member.getFirstName(), entity.getFirstName());
        assertEquals(member.getMiddleName(), entity.getMiddleName());
        assertEquals(member.getLastName(), entity.getLastName());
        assertEquals(member.getSuffix(), entity.getSuffix());
        assertEquals(member.getBirthDate(), entity.getBirthDate());
        assertEquals(member.getJoinedDate(), entity.getJoinedDate());
        assertEquals(0, entity.getEmails().size());
        assertEquals(0, entity.getAddresses().size());
        assertEquals(0, entity.getPhones().size());
        assertEquals(0, entity.getRenewals().size());

        // id should be zero
        assertEquals(0, entity.getId());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var member = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), member.getId());
        assertEquals(entity.getMemberNumber(), member.getMemberNumber());
        assertEquals(entity.getFirstName(), member.getFirstName());
        assertEquals(entity.getMiddleName(), member.getMiddleName());
        assertEquals(entity.getLastName(), member.getLastName());
        assertEquals(entity.getSuffix(), member.getSuffix());
        assertEquals(entity.getBirthDate(), member.getBirthDate());
        assertEquals(entity.getJoinedDate(), member.getJoinedDate());
    }

    @Test
    public void update_entity_from_updateDto() {
        // setup
        var update = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);


        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getMemberNumber(), updatedEntity.getMemberNumber());
        assertEquals(update.getFirstName(), updatedEntity.getFirstName());
        assertEquals(update.getMiddleName(), updatedEntity.getMiddleName());
        assertEquals(update.getLastName(), updatedEntity.getLastName());
        assertEquals(update.getSuffix(), updatedEntity.getSuffix());
        assertEquals(update.getBirthDate(), updatedEntity.getBirthDate());
        assertEquals(update.getJoinedDate(), updatedEntity.getJoinedDate());
        assertEquals(entity.getAddresses(), updatedEntity.getAddresses());
        assertEquals(entity.getEmails(), updatedEntity.getEmails());
        assertEquals(entity.getPhones(), updatedEntity.getPhones());
        assertEquals(entity.getRenewals(), updatedEntity.getRenewals());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var entityList = Instancio.ofList(MemberEntity.class)
                .size(size)
                .withUnique(field(MemberEntity::getId))
                .create();

        // execute
        var memberList = mapper.toDtoList(entityList);

        // verify
        assertEquals(entityList.size(), memberList.size());

        for (var member : memberList) {
            var optional = entityList.stream()
                    .filter(e -> e.getId() == member.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entity = optional.get();

            assertEquals(entity.getId(), member.getId());
            assertEquals(entity.getMemberNumber(), member.getMemberNumber());
            assertEquals(entity.getFirstName(), member.getFirstName());
            assertEquals(entity.getMiddleName(), member.getMiddleName());
            assertEquals(entity.getLastName(), member.getLastName());
            assertEquals(entity.getSuffix(), member.getSuffix());
            assertEquals(entity.getBirthDate(), member.getBirthDate());
            assertEquals(entity.getJoinedDate(), member.getJoinedDate());
        }
    }

    @Test
    public void from_entity_to_memberDetail() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

        // verify
        assertEquals(entity.getId(), detail.getId());
        assertEquals(entity.getMemberNumber(), detail.getMemberNumber());
        assertEquals(entity.getFirstName(), detail.getFirstName());
        assertEquals(entity.getMiddleName(), detail.getMiddleName());
        assertEquals(entity.getLastName(), detail.getLastName());
        assertEquals(entity.getSuffix(), detail.getSuffix());
        assertEquals(entity.getBirthDate(), detail.getBirthDate());
        assertEquals(entity.getJoinedDate(), detail.getJoinedDate());
    }

    @Test
    public void from_entityLit_to_detailList_addresses_match() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

        // verify

        assertFalse(detail.getAddresses().isEmpty());
        assertEquals(entity.getAddresses().size(), detail.getAddresses().size());

        for (var detailAddress : detail.getAddresses()) {
            var optional = entity.getAddresses().stream()
                    .filter(e -> e.getId() == detailAddress.getId()).findFirst();

            assertTrue(optional.isPresent());

            var addressEntity = optional.get();

            assertEquals(addressEntity.getId(), detailAddress.getId());
            assertEquals(addressEntity.getMember().getId(), detailAddress.getMemberId());
            assertEquals(addressEntity.getAddress1(), detailAddress.getAddress1());
            assertEquals(addressEntity.getAddress2(), detailAddress.getAddress2());
            assertEquals(addressEntity.getCity(), detailAddress.getCity());
            assertEquals(addressEntity.getState(), detailAddress.getState());
            assertEquals(addressEntity.getZip(), detailAddress.getZip());
            assertEquals(addressEntity.getAddressType(), detailAddress.getAddressType());
        }
    }

    @Test
    public void from_entityLit_to_detailList_emails_match() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

        // verify
        assertEquals(entity.getEmails().size(), detail.getEmails().size());

        for (var detailEmail : detail.getEmails()) {
            var optional = entity.getEmails().stream()
                    .filter(e -> e.getId() == detailEmail.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entityEmail = optional.get();

            assertEquals(entityEmail.getId(), detailEmail.getId());
            assertEquals(entityEmail.getMember().getId(), detailEmail.getMemberId());
            assertEquals(entityEmail.getEmail(), detailEmail.getEmail());
            assertEquals(entityEmail.getEmailType(), detailEmail.getEmailType());
        }
    }

    @Test
    public void from_entityLit_to_detailList_phones_match() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

        //verify
        assertEquals(entity.getPhones().size(), detail.getPhones().size());

        for (var detailPhone : detail.getPhones()) {
            var optional = entity.getPhones().stream()
                    .filter(e -> e.getId() == detailPhone.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entityPhone = optional.get();

            assertEquals(entityPhone.getId(), detailPhone.getId());
            assertEquals(entityPhone.getMember().getId(), detailPhone.getMemberId());
            assertEquals(entityPhone.getPhoneNumber(), detailPhone.getPhoneNumber());
            assertEquals(entityPhone.getPhoneType(), detailPhone.getPhoneType());
        }
    }

    @Test
    public void from_entityLit_to_detailList_renewals_match() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

        //verify
        assertEquals(entity.getRenewals().size(), detail.getRenewals().size());

        for (var detailRenew : detail.getRenewals()) {
            var optional = entity.getRenewals().stream()
                    .filter(e -> e.getId() == detailRenew.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entityRenew = optional.get();

            assertEquals(entityRenew.getId(), detailRenew.getId());
            assertEquals(entityRenew.getMember().getId(), detailRenew.getMemberId());
            assertEquals(entityRenew.getRenewalDate(), detailRenew.getRenewalDate());
            assertEquals(entityRenew.getRenewalYear(), detailRenew.getRenewalYear());
        }
    }
}
