package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

import static com.smh.club.api.helpers.datacreators.AddressCreators.createAddressEntity;
import static com.smh.club.api.helpers.datacreators.EmailCreators.*;
import static com.smh.club.api.helpers.datacreators.MemberCreators.createMemberEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmailMapperTests {
    private EmailMapperImpl mapper;

    @BeforeEach
    public void initMapper() {

        this.mapper = new EmailMapperImpl(new MapperConfig().createModelMapper());
    }

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = createEmailCreateDto(0);

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getEmail(), entity.getEmail());
        assertEquals(create.getEmailType(), entity.getEmailType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var entity = createEmailEntity(5);
        var member = createMemberEntity(10);
        entity.setMember(member);

        // execute
        var dataObject = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), dataObject.getId());
        assertEquals(entity.getMember().getId(), dataObject.getMemberId());
        assertEquals(entity.getEmail(), dataObject.getEmail());
        assertEquals(entity.getEmailType(), dataObject.getEmailType());
    }

    @Test
    public void update_entity_from_createDto() {
        // setup
        var update = createEmailCreateDto(10);
        var entity = createEmailEntity(5);

        // execute
        mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getEmail(), update.getEmail());
        assertEquals(entity.getEmailType(), update.getEmailType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void emailMapper_toDataObjectList(int size) {
        // setup
        var entityList = createEmailEntityList(size);
        entityList.sort(Comparator.comparingInt(EmailEntity::getId));

        // execute
        var dataObjectList = mapper.toDtoList(entityList);
        dataObjectList.sort(Comparator.comparingInt(EmailDto::getId));

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
}
