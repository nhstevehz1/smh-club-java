package com.smh.club.api.mappers;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.models.Renewal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RenewalMapperTests {

    private RenewalMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new RenewalMapperImpl();
    }

    @Test
    public void mapper_toEntity() {
        // setup
        var dataObject = createDataObject();

        // execute
        var entity = mapper.toEntity(dataObject);

        // verify
        assertEquals(dataObject.getRenewalDate(), entity.getRenewalDate());
        assertEquals(dataObject.getRenewalYear(), entity.getRenewalYear());

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
        assertEquals(entity.getRenewalDate(), dataObject.getRenewalDate());
        assertEquals(entity.getRenewalYear(), dataObject.getRenewalYear());
    }

    @Test
    public void mapper_updateEntity() {
        // setup
        var dataObject = createDataObject();
        var entity = createEntity();

        // execute
        mapper.update(dataObject, entity);

        // verify
        assertEquals(entity.getRenewalDate(), dataObject.getRenewalDate());
        assertEquals(entity.getRenewalYear(), dataObject.getRenewalYear());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void mapper_toDataObjectList(int size) {
        // setup
        var entityList = createEntityList(size);
        entityList.sort(Comparator.comparingInt(RenewalEntity::getId));

        // execute
        var dataObjectList = mapper.toDataObjectList(entityList);
        dataObjectList.sort(Comparator.comparingInt(Renewal::getId));

        // verify
        assertEquals(entityList.size(), dataObjectList.size());
        for (int ii = 0; ii < dataObjectList.size(); ii++) {
            var entity = entityList.get(ii);
            var dataObject = dataObjectList.get(ii);

            assertEquals(entity.getId(), dataObject.getId());
            assertEquals(entity.getMember().getId(), dataObject.getMemberId());
            assertEquals(entity.getRenewalDate(), dataObject.getRenewalDate());
            assertEquals(entity.getRenewalYear(), dataObject.getRenewalYear());
        }
    }

    private RenewalEntity createEntity() {
        var date = LocalDate.now();
        return RenewalEntity.builder()
                .renewalDate(date)
                .renewalYear(String.valueOf(date.getYear()))
                .build();
    }

    private Renewal createDataObject() {
        var date = LocalDate.now();
        return Renewal.builder()
                .renewalDate(date)
                .renewalYear(String.valueOf(date.getYear()))
                .build();
    }

    private List<RenewalEntity> createEntityList(int size) {
        List<RenewalEntity> entityList = new ArrayList<>();
        for (int ii = 0; ii < size; ii++) {
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
