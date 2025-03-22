package com.smh.club.api.mappers;

import com.smh.club.api.config.MapperConfig;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.dto.phone.PhoneDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class PhoneMapperTests {

    private final PhoneMapperImpl mapper
            = new PhoneMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = Instancio.create(PhoneCreateDto.class);

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(create.getPhoneType().getPhoneTypeName(),
            entity.getPhoneType().getPhoneTypeName());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var ret = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), ret.getId());
        assertEquals(entity.getMember().getId(), ret.getMemberId());
        assertEquals(entity.getPhoneNumber(), ret.getPhoneNumber());
        assertEquals(entity.getPhoneType().getPhoneTypeName(),
            ret.getPhoneType().getPhoneTypeName());
    }

    @Test
    public void from_entity_to_phoneMemberDto() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var ret = mapper.toPhoneMemberDto(entity);

        // verify
        assertEquals(entity.getId(), ret.getId());
        assertEquals(entity.getPhoneNumber(), ret.getPhoneNumber());
        assertEquals(entity.getPhoneType().getPhoneTypeName(),
            ret.getPhoneType().getPhoneTypeName());
        assertEquals(entity.getMember().getMemberNumber(), ret.getMemberNumber());
        assertEquals(entity.getMember().getFirstName(), ret.getFullName().getFirstName());
        assertEquals(entity.getMember().getMiddleName(), ret.getFullName().getMiddleName());
        assertEquals(entity.getMember().getLastName(), ret.getFullName().getLastName());
        assertEquals(entity.getMember().getSuffix(), ret.getFullName().getSuffix());
    }

    @Test
    public void update_entity_from_updateDto() {
        // setup
        var update = Instancio.create(PhoneDto.class);
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getId(), updatedEntity.getId());
        assertEquals(entity.getMember(), updatedEntity.getMember());
        assertEquals(update.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(update.getPhoneType().getPhoneTypeName(),
            entity.getPhoneType().getPhoneTypeName());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var entityList = Instancio.ofList(PhoneEntity.class)
                .size(size)
                .withUnique(field(PhoneEntity::getId))
                .create();

        // execute
        var phoneList = mapper.toDtoList(entityList);

        // verify
        assertEquals(entityList.size(), phoneList.size());

        for (var phone : phoneList) {
            var optional = entityList.stream()
                    .filter(e -> e.getId() == phone.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entity = optional.get();

            assertEquals(entity.getId(), phone.getId());
            assertEquals(entity.getMember().getId(), phone.getMemberId());
            assertEquals(entity.getPhoneNumber(), phone.getPhoneNumber());
            assertEquals(entity.getPhoneType().getPhoneTypeName(),
                phone.getPhoneType().getPhoneTypeName());
        }
    }
}
