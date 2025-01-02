package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.MemberModel;
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
public class MemberMapperTests {
    private final MemberMapperImpl mapper
            = new MemberMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);

    @Test
    public void from_model_to_entity() {
        // setup
        var member = Instancio.create(MemberModel.class);

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
    public void from_entity_to_model() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var member = mapper.toModel(entity);

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
    public void update_entity_from_model() {
        // setup
        var update = Instancio.create(MemberModel.class);
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
    public void from_entityList_to_modelList(int size) {
        // setup
        var entityList = Instancio.ofList(MemberEntity.class)
                .size(size)
                .withUnique(field(MemberEntity::getId))
                .create();

        // execute
        var memberList = mapper.toModelList(entityList);

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
}
