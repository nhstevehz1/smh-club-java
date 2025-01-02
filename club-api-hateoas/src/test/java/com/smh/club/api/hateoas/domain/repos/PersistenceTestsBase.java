package com.smh.club.api.hateoas.domain.repos;

import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import java.util.List;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public abstract class PersistenceTestsBase {

    protected List<MemberEntity> createMembers() {
        return Instancio.ofList(MemberEntity.class)
                .size(5)
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
    }
}
