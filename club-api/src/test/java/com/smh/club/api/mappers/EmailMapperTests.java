package com.smh.club.api.mappers;

import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.models.Email;
import com.smh.club.api.models.EmailType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmailMapperTests {
    private EmailMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new EmailMapperImpl();
    }

    @Test
    public void mapper_toEntity() {
        // setup
        var dataObject = createDataObject();

        // execute
        var entity = mapper.toEntity(dataObject);

        // verify
        assertEquals(dataObject.getEmail(), entity.getEmail());
        assertEquals(dataObject.getEmailType(), entity.getEmailType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void emailMapper_toDataObject() {
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
        assertEquals(entity.getEmail(), dataObject.getEmail());
        assertEquals(entity.getEmailType(), dataObject.getEmailType());
    }

    @Test
    public void emailMapper_updateEntity() {
        // setup
        var dataObject = createDataObject();
        var entity = createEntity();

        // execute
        mapper.updateEntity(dataObject, entity);

        // verify
        assertEquals(entity.getEmail(), dataObject.getEmail());
        assertEquals(entity.getEmailType(), dataObject.getEmailType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void emailMapper_toDataObjectList(int size) {
        // setup
        var entityList = createEntityList(size);
        entityList.sort(Comparator.comparingInt(EmailEntity::getId));

        // execute
        var dataObjectList = mapper.toDataObjectList(entityList);
        dataObjectList.sort(Comparator.comparingInt(Email::getId));

        // verify
        assertEquals(entityList.size(), dataObjectList.size());
        for (int ii = 0; ii < dataObjectList.size(); ii++) {
            var entity = entityList.get(ii);
            var dataObject = dataObjectList.get(ii);

            assertEquals(entity.getId(), dataObject.getId());
            assertEquals(entity.getMember().getId(), dataObject.getMemberId());
            assertEquals(entity.getEmail(), dataObject.getEmail());
            assertEquals(entity.getEmailType(), dataObject.getEmailType());
        }
    }

    private EmailEntity createEntity() {
        return EmailEntity.builder()
                .email("test-ent@test.com")
                .emailType(EmailType.Home)
                .build();
    }

    private Email createDataObject() {
        return Email.builder()
                .email("test-add#test.com")
                .emailType(EmailType.Other)
                .build();
    }

    private List<EmailEntity> createEntityList(int size) {
        List<EmailEntity> entityList = new ArrayList<>();
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
