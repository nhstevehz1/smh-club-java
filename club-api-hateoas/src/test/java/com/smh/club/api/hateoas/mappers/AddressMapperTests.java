package com.smh.club.api.hateoas.mappers;

import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.models.AddressModel;
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
public class AddressMapperTests {
    private final AddressMapperImpl mapper
        = new AddressMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_model_to_entity() {
        // setup
        var address = Instancio.create(AddressModel.class);

        // execute
        var entity = mapper.toEntity(address);

        // verify
        assertEquals(address.getAddress1(), entity.getAddress1());
        assertEquals(address.getAddress2(), entity.getAddress2());
        assertEquals(address.getCity(), entity.getCity());
        assertEquals(address.getState(), entity.getState());
        assertEquals(address.getZip(), entity.getZip());
        assertEquals(address.getAddressType(), entity.getAddressType());

        // id should be zero
        assertEquals(0, entity.getId());
    }

    @Test
    public void from_entity_to_model() {
        // setup
        var entity = Instancio.create(AddressEntity.class);

        // execute
        var address = mapper.toModel(entity);

        // verify
        assertEquals(entity.getId(), address.getId());
        assertEquals(entity.getMember().getId(), address.getMemberId());
        assertEquals(entity.getAddress1(), address.getAddress1());
        assertEquals(entity.getAddress2(), address.getAddress2());
        assertEquals(entity.getCity(), address.getCity());
        assertEquals(entity.getState(), address.getState());
        assertEquals(entity.getZip(), address.getZip());
        assertEquals(entity.getAddressType(), address.getAddressType());
    }

    @Test
    public void update_entity_from_model() {
        // setup
        var entity = Instancio.create(AddressEntity.class);
        var update = Instancio.create(AddressModel.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getAddress1(), updatedEntity.getAddress1());
        assertEquals(update.getAddress2(), updatedEntity.getAddress2());
        assertEquals(update.getCity(), updatedEntity.getCity());
        assertEquals(update.getState(), updatedEntity.getState());
        assertEquals(update.getZip(), updatedEntity.getZip());
        assertEquals(update.getAddressType(), updatedEntity.getAddressType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_modelList(int size) {

        // setup
        var entityList = Instancio.ofList(AddressEntity.class)
            .size(size)
            .withUnique(field(MemberEntity::getId))
            .create();

        // execute
        var addressList = mapper.toModelList(entityList);

        // verify
        assertEquals(entityList.size(), addressList.size());

        for (var address : addressList) {
            var optional = entityList.stream()
                .filter(e -> e.getId() == address.getId()).findFirst();

            assertTrue(optional.isPresent());
            var entity = optional.get();

            assertEquals(entity.getId(), address.getId());
            assertEquals(entity.getMember().getId(), address.getMemberId());
            assertEquals(entity.getAddress1(), address.getAddress1());
            assertEquals(entity.getAddress2(), address.getAddress2());
            assertEquals(entity.getCity(), address.getCity());
            assertEquals(entity.getState(), address.getState());
            assertEquals(entity.getZip(), address.getZip());
            assertEquals(entity.getAddressType(), address.getAddressType());
        }
    }
}
