package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.helpers.datacreators.AddressCreators;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

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
    public void from_addressEntity_to_addressCreateDto() {
        // setup
        var address = AddressCreators.createAddressCreateDto(1);

        // execute
        var entity = mapper.toAddressEntity(address);

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
    public void from_entity_to_addressDto() {
        // setup
        var member = MemberCreators.createMemberEntity(10);
        var entity = AddressCreators.createEntity(5, member);

        // execute
        var dataObject = mapper.toAddressDto(entity);

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
    public void update_addressEntity_from_createAddressDto() {
        // setup
        var address = AddressCreators.createAddressCreateDto(1);
        var entity = AddressCreators.createEntity(1);

        // execute
        mapper.updateAddressEntity(address, entity);

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
    public void to_addressDtoList_from_entityList(int size) {
        // setup
        var member = MemberCreators.createMemberEntity(1);
        var entityList = AddressCreators.createEntityList(size, member);
        entityList.sort(Comparator.comparingInt(AddressEntity::getId));

        // execute
        var dataObjectList = mapper.toAddressDtoList(entityList);
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
