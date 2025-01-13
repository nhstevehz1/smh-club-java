package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.rest.dto.PhoneDto;
import com.smh.club.api.rest.dto.PhoneMemberDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps phone objects
 */
public interface PhoneMapper {

    /**
     * Maps a {@link PhoneDto} to a {@link PhoneEntity}.
     *
     * @param dto The {@link PhoneDto} to map.
     * @return The resulting {@link PhoneEntity}.
     */
    PhoneEntity toEntity(PhoneDto dto);

    /**
     * Maps a {@link PhoneEntity} to a {@link PhoneDto}
     *
     * @param entity {@link PhoneEntity} to map.
     * @return The resulting {@link PhoneDto}.
     */
    PhoneDto toDto(PhoneEntity entity);

    /**
     * Maps a {@link PhoneEntity} to a {@link PhoneMemberDto}
     *
     * @param entity {@link PhoneEntity} to map.
     * @return The resulting {@link PhoneMemberDto}.
     */
    PhoneMemberDto toPhoneMemberDto(PhoneEntity entity);

    /**
     * Updates a phone object
     * @param dto The {@link PhoneDto} containing the updates.
     *
     * @param entity The {@link PhoneEntity} to be updated.
     * @return The updated {@link PhoneEntity}.
     */
    PhoneEntity updateEntity(PhoneDto dto, PhoneEntity entity);

    /**
     * Maps a list of {@link PhoneEntity} to a list of {@link PhoneDto}.
     *
     * @param entityList the {@link List} of {@link PhoneEntity} to map.
     * @return A list of {@link PhoneDto}.
     */
    List<PhoneDto> toDtoList(List<PhoneEntity> entityList);

    /**
     * Maps a page of address entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link PhoneEntity} to map.
     * @return A page of {@link PhoneMemberDto}
     */
    Page<PhoneMemberDto> toPage(Page<PhoneEntity> page);
}
