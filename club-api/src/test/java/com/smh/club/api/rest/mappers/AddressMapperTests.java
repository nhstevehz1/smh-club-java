package com.smh.club.api.rest.mappers;

import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.rest.config.MapperConfig;
import com.smh.club.api.rest.dto.AddressDto;
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
public class AddressMapperTests {

    private final AddressMapperImpl mapper =
            new AddressMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void from_createDto_to_entity() {
        // setup
        var address = Instancio.create(AddressDto.class);

        // execute
        var entity = mapper.toEntity(address);

        // verify
        assertNull(entity.getMember());
        assertEquals(address.getAddress1(), entity.getAddress1());
        assertEquals(address.getAddress2(), entity.getAddress2());
        assertEquals(address.getCity(), entity.getCity());
        assertEquals(address.getState(), entity.getState());
        assertEquals(address.getZip(), entity.getZip());
        assertEquals(address.getAddressType(), entity.getAddressType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }


    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = Instancio.create(AddressEntity.class);

        // execute
        var address = mapper.toDto(entity);

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
    public void update_entity_from_updateDto() {
        // setup
        var entity = Instancio.create(AddressEntity.class);
        var update = Instancio.create(AddressDto.class);

        // execute
        var updatedEntity = mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getId(), updatedEntity.getId());

        assertEquals(update.getAddress1(), updatedEntity.getAddress1());
        assertEquals(update.getAddress2(), updatedEntity.getAddress2());
        assertEquals(update.getCity(), updatedEntity.getCity());
        assertEquals(update.getState(), updatedEntity.getState());
        assertEquals(update.getZip(), updatedEntity.getZip());
        assertEquals(update.getAddressType(), updatedEntity.getAddressType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var entityList = Instancio.ofList(AddressEntity.class)
                .size(size)
                .withUnique(field(AddressEntity::getId))
                .create();

        // execute
        var addressList = mapper.toDtoList(entityList);

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
