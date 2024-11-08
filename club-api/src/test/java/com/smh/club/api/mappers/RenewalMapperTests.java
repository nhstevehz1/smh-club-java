package com.smh.club.api.mappers;

import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.mappers.config.MapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;

import static com.smh.club.api.helpers.datacreators.MemberCreators.createMemberEntity;
import static com.smh.club.api.helpers.datacreators.RenewalCreators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RenewalMapperTests {

    private RenewalMapperImpl mapper;

    @BeforeEach
    public void initMapper() {
        this.mapper = new RenewalMapperImpl(new MapperConfig().createModelMapper());
    }

    @Test
    public void from_createDto_to_entity() {
        // setup
        var create = genCreateRenewalDto(5);

        // execute
        var entity = mapper.toEntity(create);

        // verify
        assertNull(entity.getMember());
        assertEquals(create.getRenewalDate(), entity.getRenewalDate());
        assertEquals(create.getRenewalYear(), entity.getRenewalYear());

        // id should be zero
        assertEquals(0, entity.getId());

        // member should be null
        assertNull(entity.getMember());
    }

    @Test
    public void from_entity_to_dto() {
        // setup
        var member = createMemberEntity(2);
        var entity = genRenewalEntity(12, member);


        // execute
        var ret = mapper.toDto(entity);

        // verify
        assertEquals(entity.getId(), ret.getId());
        assertEquals(entity.getMember().getId(), ret.getMemberId());
        assertEquals(entity.getRenewalDate(), ret.getRenewalDate());
        assertEquals(entity.getRenewalYear(), ret.getRenewalYear());
    }

    @Test
    public void update_entity_from_createDto() {
        // setup
        var update = genUpdateRenewalDto(6);
        var entity = genRenewalEntity(6);

        // execute
        var ret = mapper.updateEntity(update, entity);

        // verify
        assertEquals(entity.getRenewalDate(), ret.getRenewalDate());
        assertEquals(entity.getRenewalYear(), ret.getRenewalYear());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    public void from_entityList_to_dtoList(int size) {
        // setup
        var member = createMemberEntity(5);
        var entityList = genRenewalEntityList(size, member);
        entityList.sort(Comparator.comparingInt(RenewalEntity::getId));

        // execute
        var dataObjectList = mapper.toDtoList(entityList);
        dataObjectList.sort(Comparator.comparingInt(RenewalDto::getId));

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
}
