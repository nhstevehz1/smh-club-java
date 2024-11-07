package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberMapperTests {

    private MemberMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new MemberMapperImpl(new MapperConfig().createModelMapper());
    }

    @Test
    public void mapper_toMemberEntity() {
        // setup
        var dataObject = MemberCreators.createMemberCreateDto(30);

        // execute
        var entity = mapper.toMemberEntity(dataObject);

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
        var entity = MemberCreators.createMemberEntity(150);

        // execute
        var dataObject = mapper.toMemberDetailDto(entity);

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
        var dataObject = MemberCreators.createMemberCreateDto(200);
        var entity = MemberCreators.createMemberEntity(100);


        // execute
        mapper.updateMemberEntity(dataObject, entity);

        // verify
        assertEquals(dataObject.getMemberNumber(), entity.getMemberNumber());
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
        var entityList = MemberCreators.createMemeberEntityList(size);
        entityList.sort(Comparator.comparingInt(MemberEntity::getId));

        // execute
        var dataObjectList = mapper.toMemberDtoList(entityList);
        dataObjectList.sort(Comparator.comparingInt(MemberDto::getId));

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
    public void mapper_toMemberMinimum() {
        // setup
        var entity = MemberCreators.createMemberEntity(20);

        // execute
        var detail = mapper.toMemberDetailDto(entity);

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
}
