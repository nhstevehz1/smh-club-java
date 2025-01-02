package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.models.MemberModel;
import java.util.List;

/**
 * Maps member objects
 */
public interface MemberMapper {

    /**
     * Maps a {@link MemberModel} to a {@link MemberEntity}.
     * @param model The {@link MemberModel} to map.
     * @return The resulting {@link MemberEntity}.
     */
    MemberEntity toEntity(MemberModel model);

    /**
     * Maps a {@link MemberEntity} to a {@link MemberModel}
     * @param entity {@link MemberEntity} to map.
     * @return The resulting {@link MemberModel}.
     */
    MemberModel toModel(MemberEntity entity);

    /**
     * Updates a member object
     * @param model The {@link MemberModel} containing the updates. 
     * @param entity The {@link MemberEntity} to be updated.
     * @return The updated {@link MemberEntity}.
     */
    MemberEntity updateEntity(MemberModel model, MemberEntity entity);

    /**
     * Maps a list of {@link MemberEntity} to a list of {@link MemberModel}.
     * @param entityList the {@link List} of {@link MemberEntity} to map.
     * @return A list of {@link MemberModel}.
     */
    List<MemberModel> toModelList(List<MemberEntity> entityList);
}
