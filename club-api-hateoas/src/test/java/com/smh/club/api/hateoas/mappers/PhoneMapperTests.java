package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;
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
    public void from_model_to_entity() {
        // setup
        var phone = Instancio.create(PhoneModel.class);

        // execute
        var entity = mapper.toEntity(phone);

        // verify
        assertEquals(phone.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(phone.getPhoneType(), entity.getPhoneType());

        // id should be zero
        assertEquals(0, entity.getId());
    }

    @Test
    public void from_entity_to_model() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var phone = mapper.toModel(entity);

        // verify
        assertEquals(entity.getId(), phone.getId());
        assertEquals(entity.getMember().getId(), phone.getMemberId());
        assertEquals(entity.getPhoneNumber(), phone.getPhoneNumber());
        assertEquals(entity.getPhoneType(), phone.getPhoneType());
    }

    @Test
    public void update_entity_from_model() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);
        var update = Instancio.create(PhoneModel.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getPhoneNumber(), updatedEntity.getPhoneNumber());
        assertEquals(update.getPhoneType(), updatedEntity.getPhoneType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_modelList(int size) {

        // setup
        var entityList = Instancio.ofList(PhoneEntity.class)
            .size(size)
            .withUnique(field(MemberEntity::getId))
            .create();

        // execute
        var phoneList = mapper.toModelList(entityList);

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
            assertEquals(entity.getPhoneType(), phone.getPhoneType());
        }
    }
}
