package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

import static com.smh.club.api.helpers.datacreators.MemberCreators.createMemberEntity;
import static com.smh.club.api.helpers.datacreators.PhoneCreators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PhoneMapperTests {

    private PhoneMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new PhoneMapperImpl(new MapperConfig().createModelMapper());
    }

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = genCreatePhoneDto(0);

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getPhoneNum(), entity.getPhoneNum());
        assertEquals(create.getPhoneType(), entity.getPhoneType());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var member = createMemberEntity(10);
        var entity = genPhoneEntity(5, member);

        // execute
        var ret = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), ret.getId());
        assertEquals(entity.getMember().getId(), ret.getMemberId());
        assertEquals(entity.getPhoneNum(), ret.getPhoneNum());
        assertEquals(entity.getPhoneType(), ret.getPhoneType());
    }

    @Test
    public void update_entity_from_createDto() {
        // setup
        var update = genUpdatePhoneDto(10);
        var entity = genPhoneEntity(5);

        // execute
        mapper.updateEntity(update, entity);

        // verify
        assertEquals(update.getPhoneNum(), entity.getPhoneNum());
        assertEquals(update.getPhoneType(), entity.getPhoneType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var member = createMemberEntity(1);
        var entityList = genPhoneEntityList(size, member);
        entityList.sort(Comparator.comparingInt(PhoneEntity::getId));

        // execute
        var phoneDtoList = mapper.toDtoList(entityList);
        phoneDtoList.sort(Comparator.comparingInt(PhoneDto::getId));

        // verify
        assertEquals(entityList.size(), phoneDtoList.size());
        for (int ii = 0; ii < phoneDtoList.size(); ii++) {
            var entity = entityList.get(ii);
            var dataObject = phoneDtoList.get(ii);

            assertEquals(entity.getId(), dataObject.getId());
            assertEquals(entity.getMember().getId(), dataObject.getMemberId());
            assertEquals(entity.getPhoneNum(), dataObject.getPhoneNum());
            assertEquals(entity.getPhoneType(), dataObject.getPhoneType());
        }
    }

}
