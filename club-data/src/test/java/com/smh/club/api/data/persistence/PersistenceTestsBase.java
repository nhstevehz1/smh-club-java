package com.smh.club.api.data.persistence;

import com.smh.club.api.data.entities.MemberEntity;
import java.util.List;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public abstract class PersistenceTestsBase {

    protected MemberEntity createMember() {
        return Instancio.of(MemberEntity.class)
                .ignore(field(MemberEntity::getId))
                .create();
    }

    protected List<MemberEntity> createMembers(int size) {
        return Instancio.ofList(MemberEntity.class)
                .size(size)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
    }
}
