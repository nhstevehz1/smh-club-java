package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

import static com.smh.club.api.helpers.datacreators.AddressCreators.*;
import static com.smh.club.api.helpers.datacreators.MemberCreators.createMemberEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressMapperTests {

    private AddressMapperImpl mapper;

    @BeforeAll
    public void initMapper() {
        this.mapper = new AddressMapperImpl(new MapperConfig().createModelMapper());
    }

    @Test
    public void from_createDto_to_entity() {
        // setup
        var address = genCreateAddressDto(1);

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
        var member = createMemberEntity(10);
        var entity = genAddressEntity(5, member);

        // execute
        var dataObject = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), dataObject.getId());
        assertEquals(entity.getMember().getId(), dataObject.getMemberId());
        assertEquals(entity.getAddress1(), dataObject.getAddress1());
        assertEquals(entity.getAddress2(), dataObject.getAddress2());
        assertEquals(entity.getCity(), dataObject.getCity());
        assertEquals(entity.getState(), dataObject.getState());
        assertEquals(entity.getZip(), dataObject.getZip());
        assertEquals(entity.getAddressType(), dataObject.getAddressType());
    }

    @Test
    public void update_entity_from_updateDto() {
        // setup
        var address = genUpdateAddressDto(1);
        var entity = genAddressEntity(1);

        // execute
        mapper.updateEntity(address, entity);

        // verify
        assertEquals(entity.getAddress1(), address.getAddress1());
        assertEquals(entity.getAddress2(), address.getAddress2());
        assertEquals(entity.getCity(), address.getCity());
        assertEquals(entity.getState(), address.getState());
        assertEquals(entity.getZip(), address.getZip());
        assertEquals(entity.getAddressType(), address.getAddressType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var member = createMemberEntity(1);
        var entityList = genAddressEntityList(size, member);
        entityList.sort(Comparator.comparingInt(AddressEntity::getId));

        // execute
        var dataObjectList = mapper.toDtoList(entityList);
        dataObjectList.sort(Comparator.comparingInt(AddressDto::getId));

        // verify
        assertEquals(entityList.size(), dataObjectList.size());
        for (int ii = 0; ii < dataObjectList.size(); ii++) {
            var entity = entityList.get(ii);
            var dataObject = dataObjectList.get(ii);

            assertEquals(entity.getId(), dataObject.getId());
            assertEquals(entity.getMember().getId(), dataObject.getMemberId());
            assertEquals(entity.getAddress1(), dataObject.getAddress1());
            assertEquals(entity.getAddress2(), dataObject.getAddress2());
            assertEquals(entity.getCity(), dataObject.getCity());
            assertEquals(entity.getState(), dataObject.getState());
            assertEquals(entity.getZip(), dataObject.getZip());
            assertEquals(entity.getAddressType(), dataObject.getAddressType());

        }
    }

}
