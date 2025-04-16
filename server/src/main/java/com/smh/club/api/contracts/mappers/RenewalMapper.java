package com.smh.club.api.contracts.mappers;

import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalMemberDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Maps renewal objects.
 */
public interface RenewalMapper {

    /**
     * Maps a {@link RenewalDto} to a {@link RenewalEntity}.
     *
     * @param dto The {@link RenewalDto} to map.
     * @return The resulting {@link RenewalEntity}.
     */
    RenewalEntity toEntity(RenewalDto dto);

    /**
     * Maps a {@link RenewalEntity} to a {@link RenewalDto}.
     *
     * @param entity {@link RenewalEntity} to map.
     * @return The resulting {@link RenewalDto}.
     */
    RenewalDto toDto(RenewalEntity entity);

    /**
     * Maps a {@link RenewalEntity} to a {@link RenewalMemberDto}.
     *
     * @param entity {@link RenewalEntity} to map.
     * @return The resulting {@link RenewalMemberDto}.
     */
    RenewalMemberDto toRenewalMemberDto(RenewalEntity entity);

    /**
     * Updates a renewal object
     * @param dto The {@link RenewalDto} containing the updates.
     *
     * @param entity The {@link RenewalEntity} to be updated.
     * @return The updated {@link RenewalEntity}.
     */
    RenewalEntity updateEntity(RenewalDto dto, RenewalEntity entity);

    /**
     * Maps a list of {@link RenewalEntity} to a list of {@link RenewalDto}.
     *
     * @param entityList the {@link List} of {@link RenewalEntity} to map.
     * @return A list of {@link RenewalDto}.
     */
    List<RenewalDto> toDtoList(List<RenewalEntity> entityList);

    /**
     * Maps a page of address entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link RenewalEntity} to map.
     * @return A page of {@link RenewalMemberDto}
     */
    Page<RenewalMemberDto> toPage(Page<RenewalEntity> page);
}
