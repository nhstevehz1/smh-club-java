package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;
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
public class RenewalMapperTests {

    private final RenewalMapperImpl mapper
        = new RenewalMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_model_to_entity() {
        // setup
        var renewal = Instancio.create(RenewalModel.class);

        // execute
        var entity = mapper.toEntity(renewal);

        // verify
        assertEquals(renewal.getRenewalDate(), entity.getRenewalDate());
        assertEquals(renewal.getRenewalYear(), entity.getRenewalYear());

        // id should be zero
        assertEquals(0, entity.getId());
    }

    @Test
    public void from_entity_to_model() {
        // setup
        var entity = Instancio.create(RenewalEntity.class);

        // execute
        var renewal = mapper.toModel(entity);

        // verify
        assertEquals(entity.getId(), renewal.getId());
        assertEquals(entity.getMember().getId(), renewal.getMemberId());
        assertEquals(entity.getRenewalDate(), renewal.getRenewalDate());
        assertEquals(entity.getRenewalYear(), renewal.getRenewalYear());
    }

    @Test
    public void update_entity_from_model() {
        // setup
        var entity = Instancio.create(RenewalEntity.class);
        var update = Instancio.create(RenewalModel.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getRenewalDate(), updatedEntity.getRenewalDate());
        assertEquals(update.getRenewalYear(), updatedEntity.getRenewalYear());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_modelList(int size) {

        // setup
        var entityList = Instancio.ofList(RenewalEntity.class)
            .size(size)
            .withUnique(field(MemberEntity::getId))
            .create();

        // execute
        var renewalList = mapper.toModelList(entityList);

        // verify
        assertEquals(entityList.size(), renewalList.size());

        for (var renewal : renewalList) {
            var optional = entityList.stream()
                .filter(e -> e.getId() == renewal.getId()).findFirst();

            assertTrue(optional.isPresent());
            var entity = optional.get();

            assertEquals(entity.getId(), renewal.getId());
            assertEquals(entity.getMember().getId(), renewal.getMemberId());
            assertEquals(entity.getRenewalDate(), renewal.getRenewalDate());
            assertEquals(entity.getRenewalYear(), renewal.getRenewalYear());
        }
    }
}
