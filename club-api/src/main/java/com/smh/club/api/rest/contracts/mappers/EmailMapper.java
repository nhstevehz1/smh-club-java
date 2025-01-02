package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.dto.AddressDto;
import com.smh.club.api.rest.dto.EmailDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps email objects
 */
public interface EmailMapper {

    /**
     * Maps a {@link EmailDto} to a {@link EmailEntity}.
     * @param dto The {@link EmailDto} to map.
     * @return The resulting {@link EmailEntity}.
     */
    EmailEntity toEntity(EmailDto dto);

    /**
     * Maps a {@link EmailEntity} to a {@link EmailDto}
     * @param entity {@link EmailEntity} to map.
     * @return The resulting {@link EmailDto}.
     */
    EmailDto toDto(EmailEntity entity);

    /**
     * Updates a email object
     * @param dto The {@link EmailDto} containing the updates.
     * @param entity The {@link EmailEntity} to be updated.
     * @return The updated {@link EmailEntity}.
     */
    EmailEntity updateEntity(EmailDto dto, EmailEntity entity);

    /**
     * Maps a list of {@link EmailEntity} to a list of {@link EmailDto}.
     * @param entityList the {@link List} of {@link EmailEntity} to map.
     * @return A list of {@link EmailDto}.
     */
    List<EmailDto> toDtoList(List<EmailEntity> entityList);

    /**
     * Maps a page of address entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link AddressEntity} to map.
     * @return A page of {@link AddressDto}
     */
    Page<EmailDto> toPage(Page<EmailEntity> page);
}
