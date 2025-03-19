package com.smh.club.api.contracts.mappers;

import com.smh.club.api.rest.domain.entities.MemberEntity;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps member objects
 */
public interface MemberMapper {

    /**
     * Maps a {@link MemberCreateDto} to a {@link MemberEntity}.
     * @param dto The {@link MemberBaseDto} to map.
     * @return The resulting {@link MemberEntity}.
     */
    MemberEntity toEntity(MemberCreateDto dto);

    /**
     * Maps a {@link MemberEntity} to a {@link MemberBaseDto}
     *
     * @param entity {@link MemberEntity} to map.
     * @return The resulting {@link MemberDto}.
     */
    MemberDto toDto(MemberEntity entity);

    /**
     * Updates a member object
     *
     * @param dto The {@link MemberUpdateDto} containing the updates.
     * @param entity The {@link MemberEntity} to be updated.
     * @return The updated {@link MemberEntity}.
     */
    MemberEntity updateEntity(MemberUpdateDto dto, MemberEntity entity);

    /**
     * Maps a list of {@link MemberEntity} to a list of {@link MemberBaseDto}.
     *
     * @param entityList the {@link List} of {@link MemberEntity} to map.
     * @return A list of {@link MemberBaseDto}.
     */
    List<MemberDto> toDtoList(List<MemberEntity> entityList);

    /**
     * Maps a page of address entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link MemberEntity} to map.
     * @return A page of {@link MemberDto}
     */
    Page<MemberDto> toPage(Page<MemberEntity> page);

    MemberDetailDto toMemberDetailDto(MemberEntity entity);
}
