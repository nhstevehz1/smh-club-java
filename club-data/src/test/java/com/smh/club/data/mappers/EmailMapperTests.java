package com.smh.club.data.mappers;

import com.smh.club.data.domain.entities.EmailEntity;
import com.smh.club.data.dto.EmailDto;
import com.smh.club.data.config.MapperConfig;
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
public class EmailMapperTests {

    private final EmailMapperImpl mapper =
            new EmailMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = Instancio.of(EmailDto.class)
                .ignore(field(EmailDto::getId))
                .create();

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getEmail(), entity.getEmail());
        assertEquals(create.getEmailType(), entity.getEmailType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = Instancio.create(EmailEntity.class);

        // execute
        var email = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), email.getId());
        assertEquals(entity.getMember().getId(), email.getMemberId());
        assertEquals(entity.getEmail(), email.getEmail());
        assertEquals(entity.getEmailType(), email.getEmailType());
    }

    @Test
    public void update_entity_from_createDto() {
        // setup
        var update = Instancio.create(EmailDto.class);
        var entity = Instancio.create(EmailEntity.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getId(), updatedEntity.getId());
        assertEquals(entity.getMember(), updatedEntity.getMember());
        assertEquals(update.getEmail(), updatedEntity.getEmail());
        assertEquals(update.getEmailType(), updatedEntity.getEmailType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var entityList = Instancio.ofList(EmailEntity.class)
                .size(size)
                .withUnique(field(EmailEntity::getId))
                .create();

        // execute
        var emailList = mapper.toDtoList(entityList);

        // verify
        assertEquals(entityList.size(), emailList.size());

        for (var email : emailList) {
            var optional = entityList.stream()
                    .filter(e -> e.getId() == email.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entity = optional.get();

            assertEquals(entity.getId(), email.getId());
            assertEquals(entity.getMember().getId(), email.getMemberId());
            assertEquals(entity.getEmail(), email.getEmail());
            assertEquals(entity.getEmailType(), email.getEmailType());
        }
    }
}
