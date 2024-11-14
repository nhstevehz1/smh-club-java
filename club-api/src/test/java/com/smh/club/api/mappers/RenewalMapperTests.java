package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
import com.smh.club.api.mappers.config.MapperConfig;
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
public class RenewalMapperTests {

    private final RenewalMapperImpl mapper =
            new RenewalMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings // Data randomizer settings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = Instancio.create(CreateRenewalDto.class);

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getRenewalDate(), entity.getRenewalDate());
        assertEquals(create.getRenewalYear(), entity.getRenewalYear());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = Instancio.create(RenewalEntity.class);
        
        // execute
        var ret = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), ret.getId());
        assertEquals(entity.getMember().getId(), ret.getMemberId());
        assertEquals(entity.getRenewalDate(), ret.getRenewalDate());
        assertEquals(entity.getRenewalYear(), ret.getRenewalYear());
    }

    @Test
    public void update_entity_from_createDto() {
        // setup
        var update = Instancio.create(UpdateRenewalDto.class);
        var entity = Instancio.create(RenewalEntity.class);

        // execute
        var ret = mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getRenewalDate(), ret.getRenewalDate());
        assertEquals(entity.getRenewalYear(), ret.getRenewalYear());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var entityList = Instancio.ofList(RenewalEntity.class)
                .size(size)
                .withUnique(field(RenewalEntity::getId))
                .create();

        // execute
        var renewList = mapper.toDtoList(entityList);

        // verify
        assertEquals(entityList.size(), renewList.size());

        for (var renew : renewList) {
            var optional = entityList.stream()
                    .filter(e -> e.getId() == renew.getId()).findFirst();

            assertTrue(optional.isPresent());

            var entity = optional.get();

            assertEquals(entity.getId(), renew.getId());
            assertEquals(entity.getMember().getId(), renew.getMemberId());
            assertEquals(entity.getRenewalDate(), renew.getRenewalDate());
            assertEquals(entity.getRenewalYear(), renew.getRenewalYear());
        }
    }
}
