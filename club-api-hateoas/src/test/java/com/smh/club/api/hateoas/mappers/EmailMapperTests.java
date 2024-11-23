package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.models.EmailModel;
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
public class EmailMapperTests {

    private final EmailMapperImpl mapper
        = new EmailMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_model_to_entity() {
        // setup
        var email = Instancio.create(EmailModel.class);

        // execute
        var entity = mapper.toEntity(email);

        // verify
        assertEquals(email.getEmail(), entity.getEmail());
        assertEquals(email.getEmailType(), entity.getEmailType());

        // id should be zero
        assertEquals(0, entity.getId());
    }

    @Test
    public void from_entity_to_model() {
        // setup
        var entity = Instancio.create(EmailEntity.class);

        // execute
        var email = mapper.toModel(entity);

        // verify
        assertEquals(entity.getId(), email.getId());
        assertEquals(entity.getMember().getId(), email.getMemberId());
        assertEquals(entity.getEmail(), email.getEmail());
        assertEquals(entity.getEmailType(), email.getEmailType());
    }

    @Test
    public void update_entity_from_model() {
        // setup
        var entity = Instancio.create(EmailEntity.class);
        var update = Instancio.create(EmailModel.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getEmail(), updatedEntity.getEmail());
        assertEquals(update.getEmailType(), updatedEntity.getEmailType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_modelList(int size) {

        // setup
        var entityList = Instancio.ofList(EmailEntity.class)
            .size(size)
            .withUnique(field(MemberEntity::getId))
            .create();

        // execute
        var emailList = mapper.toModelList(entityList);

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
