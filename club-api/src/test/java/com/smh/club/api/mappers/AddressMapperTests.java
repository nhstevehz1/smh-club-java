package com.smh.club.api.mappers;

import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.models.Address;
import com.smh.club.api.models.AddressType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressMapperTests {

    private AddressMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new AddressMapperImpl();
    }

    @Test
    public void mapper_toEntity() {
        // setup
        var dataObject = createDataObject();

        // execute
        var entity = mapper.toEntity(dataObject);

        // verify
        assertEquals(dataObject.getAddress1(), entity.getAddress1());
        assertEquals(dataObject.getAddress2(), entity.getAddress2());
        assertEquals(dataObject.getCity(), entity.getCity());
        assertEquals(dataObject.getState(), entity.getState());
        assertEquals(dataObject.getZip(), entity.getZip());
        assertEquals(dataObject.getAddressType(), entity.getAddressType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void mapper_toDataObject() {
        // setup
        var entity = createEntity();
        var member = new MemberEntity();
        member.setId(10);
        entity.setId(5);
        entity.setMember(member);

        // execute
        var dataObject = mapper.toDataObject(entity);

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
    public void mapper_updateEntity() {
        // setup
        var dataObject = createDataObject();
        var entity = createEntity();

        // execute
        mapper.updateEntity(dataObject, entity);

        // verify
        assertEquals(entity.getAddress1(), dataObject.getAddress1());
        assertEquals(entity.getAddress2(), dataObject.getAddress2());
        assertEquals(entity.getCity(), dataObject.getCity());
        assertEquals(entity.getState(), dataObject.getState());
        assertEquals(entity.getZip(), dataObject.getZip());
        assertEquals(entity.getAddressType(), dataObject.getAddressType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void mapper_toDataObjectList(int size) {
        // setup
        var entityList = createEntityList(size);
        entityList.sort(Comparator.comparingInt(AddressEntity::getId));

        // execute
        var dataObjectList = mapper.toDataObjectList(entityList);
        dataObjectList.sort(Comparator.comparingInt(Address::getId));

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

    private AddressEntity createEntity() {
        return AddressEntity.builder()
                .address1("ent_address1")
                .address2("ent_address2")
                .city("ent_city")
                .state("ent_state")
                .zip("ent_zip")
                .addressType(AddressType.Home)
                .build();
    }

    private Address createDataObject() {
        return Address.builder()
                .Address1("ad_address1")
                .Address2("ad_address2")
                .city("ad_city")
                .state("ad_state")
                .addressType(AddressType.Work)
                .build();
    }

    private List<AddressEntity> createEntityList(int size) {
        List<AddressEntity> entityList = new ArrayList<>();
        for (int ii = 0; ii < size; ii++ ) {
            MemberEntity member = new MemberEntity();
            member.setId(ii + 10);
            var entity = createEntity();
            entity.setId(ii);
            entity.setMember(member);
            entityList.add(entity);
        }

        return entityList;
    }

}
