package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.RenewalEntity;
import com.smh.club.api.rest.dto.renewal.RenewalCreateDto;
import com.smh.club.api.rest.dto.renewal.RenewalDto;
import com.smh.club.api.rest.dto.renewal.RenewalFullNameDto;
import com.smh.club.api.rest.dto.renewal.RenewalUpdateDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps renewal objects.
 */
public interface RenewalMapper {

    /**
     * Maps a {@link RenewalCreateDto} to a {@link RenewalEntity}.
     *
     * @param dto The {@link RenewalCreateDto} to map.
     * @return The resulting {@link RenewalEntity}.
     */
    RenewalEntity toEntity(RenewalCreateDto dto);

    /**
     * Maps a {@link RenewalEntity} to a {@link RenewalDto}.
     *
     * @param entity {@link RenewalEntity} to map.
     * @return The resulting {@link RenewalDto}.
     */
    RenewalDto toDto(RenewalEntity entity);

    /**
     * Maps a {@link RenewalEntity} to a {@link RenewalFullNameDto}.
     *
     * @param entity {@link RenewalEntity} to map.
     * @return The resulting {@link RenewalFullNameDto}.
     */
    RenewalFullNameDto toRenewalMemberDto(RenewalEntity entity);

    /**
     * Updates a renewal object
     * @param dto The {@link RenewalDto} containing the updates.
     *
     * @param entity The {@link RenewalEntity} to be updated.
     * @return The updated {@link RenewalEntity}.
     */
    RenewalEntity updateEntity(RenewalUpdateDto dto, RenewalEntity entity);

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
     * @return A page of {@link RenewalFullNameDto}
     */
    Page<RenewalFullNameDto> toPage(Page<RenewalEntity> page);
}
