package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps member objects
 */
public interface MemberMapper {

    /**
     * Maps a {@link MemberDto} to a {@link MemberEntity}.
     * @param dto The {@link MemberDto} to map.
     * @return The resulting {@link MemberEntity}.
     */
    MemberEntity toEntity(MemberDto dto);

    /**
     * Maps a {@link MemberEntity} to a {@link MemberDto}
     * @param entity {@link MemberEntity} to map.
     * @return The resulting {@link MemberDto}.
     */
    MemberDto toDto(MemberEntity entity);

    /**
     * Updates a member object
     * @param dto The {@link MemberDto} containing the updates. 
     * @param entity The {@link MemberEntity} to be updated.
     * @return The updated {@link MemberEntity}.
     */
    MemberEntity updateEntity(MemberDto dto, MemberEntity entity);

    /**
     * Maps a list of {@link MemberEntity} to a list of {@link MemberDto}.
     * @param entityList the {@link List} of {@link MemberEntity} to map.
     * @return A list of {@link MemberDto}.
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
