package com.smh.club.api.mappers;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.models.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberMapperTests {

    private MemberMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new MemberMapperImpl();
    }

    @Test
    public void mapper_toEntity() {
        // setup
        var dataObject = createDataObject();

        // execute
        var entity = mapper.toEntity(dataObject);

        // verify
        assertEquals(dataObject.getFirstName(), entity.getFirstName());
        assertEquals(dataObject.getMiddleName(), entity.getMiddleName());
        assertEquals(dataObject.getLastName(), entity.getLastName());
        assertEquals(dataObject.getSuffix(), entity.getSuffix());
        assertEquals(dataObject.getBirthDate(), entity.getBirthDate());
        assertEquals(dataObject.getJoinedDate(), entity.getJoinedDate());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be 0
        assertEquals(dataObject.getMemberNumber(), entity.getMemberNumber());
    }

    @Test
    public void mapper_toDataObject() {
        // setup
        var entity = createEntity();

        // execute
        var dataObject = mapper.toDataObject(entity);

        // verify
        assertEquals(entity.getId(), dataObject.getId());
        assertEquals(entity.getMemberNumber(), dataObject.getMemberNumber());
        assertEquals(entity.getFirstName(), dataObject.getFirstName());
        assertEquals(entity.getMiddleName(), dataObject.getMiddleName());
        assertEquals(entity.getLastName(), dataObject.getLastName());
        assertEquals(entity.getSuffix(), dataObject.getSuffix());
        assertEquals(entity.getBirthDate(), dataObject.getBirthDate());
        assertEquals(entity.getJoinedDate(), dataObject.getJoinedDate());
    }

    @Test
    public void mapper_updateEntity() {
        // setup
        var dataObject = createDataObject();
        dataObject.setId(100);
        dataObject.setMemberNumber(200);

        var entity = createEntity();
        entity.setId(300);
        entity.setMemberNumber(100);

        // execute
        mapper.updateEntity(dataObject, entity);

        // verify
        assertEquals(300, entity.getId());
        assertEquals(200, entity.getMemberNumber());
        assertEquals(dataObject.getFirstName(), entity.getFirstName());
        assertEquals(dataObject.getMiddleName(), entity.getMiddleName());
        assertEquals(dataObject.getLastName(), entity.getLastName());
        assertEquals(dataObject.getSuffix(), entity.getSuffix());
        assertEquals(dataObject.getBirthDate(), entity.getBirthDate());
        assertEquals(dataObject.getJoinedDate(), entity.getJoinedDate());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void mapper_toDataObjectList(int size) {
        // setup
        var entityList = createEntityList(size);
        entityList.sort(Comparator.comparingInt(MemberEntity::getId));

        // execute
        var dataObjectList = mapper.toDataObjectList(entityList);
        dataObjectList.sort(Comparator.comparingInt(Member::getId));

        // verify
        assertEquals(entityList.size(), dataObjectList.size());
        for (int ii = 0; ii < dataObjectList.size(); ii++) {
            var entity = entityList.get(ii);
            var dataObject = dataObjectList.get(ii);

            assertEquals(entity.getId(), dataObject.getId());
            assertEquals(entity.getMemberNumber(), dataObject.getMemberNumber());
            assertEquals(entity.getFirstName(), dataObject.getFirstName());
            assertEquals(entity.getMiddleName(), dataObject.getMiddleName());
            assertEquals(entity.getLastName(), dataObject.getLastName());
            assertEquals(entity.getSuffix(), dataObject.getSuffix());
            assertEquals(entity.getBirthDate(), dataObject.getBirthDate());
            assertEquals(entity.getJoinedDate(), dataObject.getJoinedDate());

        }
    }

    @Test
    public void mapper_toMemberDetail() {
        // setup
        var entity = createEntity();

        // execute
        var detail = mapper.toMemberDetail(entity);

        // verify
        assertEquals(entity.getId(), detail.getId());
        assertEquals(entity.getMemberNumber(), detail.getMemberNumber());
        assertEquals(entity.getFirstName(), detail.getFirstName());
        assertEquals(entity.getMiddleName(), detail.getMiddleName());
        assertEquals(entity.getLastName(), detail.getLastName());
        assertEquals(entity.getSuffix(), detail.getSuffix());
        assertEquals(entity.getBirthDate(), detail.getBirthDate());
        assertEquals(entity.getJoinedDate(), detail.getJoinedDate());
    }

    private MemberEntity createEntity() {
        return MemberEntity.builder()
                .memberNumber(10)
                .firstName("ent_firstName")
                .middleName("ent_middleName")
                .lastName("ent_lastName")
                .suffix("ent_suffix")
                .birthDate(LocalDate.now())
                .joinedDate(LocalDate.now().minusYears(15))
                .build();
    }

    private Member createDataObject() {
        return Member.builder()
                .memberNumber(50)
                .firstName("m_firstName")
                .middleName("m_middleName")
                .lastName("m_lastName")
                .suffix("m_suffix")
                .birthDate(LocalDate.now())
                .joinedDate(LocalDate.now().minusYears(10))
                .build();
    }

    private List<MemberEntity> createEntityList(int size) {
        List<MemberEntity> entityList = new ArrayList<>();
        for (int ii = 0; ii < size; ii++ ) {
            var entity = createEntity();
            entity.setId(ii);
            entity.setMemberNumber(ii + 50);
            entityList.add(entity);
        }

        return entityList;
    }
}
