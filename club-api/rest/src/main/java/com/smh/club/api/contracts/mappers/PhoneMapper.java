package com.smh.club.api.contracts.mappers;

import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneFullNameDto;
import com.smh.club.api.dto.phone.PhoneUpdateDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps phone objects
 */
public interface PhoneMapper {

    /**
     * Maps a {@link PhoneCreateDto} to a {@link PhoneEntity}.
     *
     * @param dto The {@link PhoneCreateDto} to map.
     * @return The resulting {@link PhoneEntity}.
     */
    PhoneEntity toEntity(PhoneCreateDto dto);

    /**
     * Maps a {@link PhoneEntity} to a {@link PhoneDto}
     *
     * @param entity {@link PhoneEntity} to map.
     * @return The resulting {@link PhoneDto}.
     */
    PhoneDto toDto(PhoneEntity entity);

    /**
     * Maps a {@link PhoneEntity} to a {@link PhoneFullNameDto}
     *
     * @param entity {@link PhoneEntity} to map.
     * @return The resulting {@link PhoneFullNameDto}.
     */
    PhoneFullNameDto toPhoneMemberDto(PhoneEntity entity);

    /**
     * Updates a phone object
     *
     * @param dto The {@link PhoneUpdateDto} containing the updates.
     *
     * @param entity The {@link PhoneEntity} to be updated.
     * @return The updated {@link PhoneEntity}.
     */
    PhoneEntity updateEntity(PhoneUpdateDto dto, PhoneEntity entity);

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
     * @return A page of {@link PhoneFullNameDto}
     */
    Page<PhoneFullNameDto> toPage(Page<PhoneEntity> page);
}
