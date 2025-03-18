package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.dto.email.EmailCreateDto;
import com.smh.club.api.rest.dto.email.EmailDto;
import com.smh.club.api.rest.dto.email.EmailFullNameDto;
import com.smh.club.api.rest.dto.email.EmailUpdateDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps email objects
 */
public interface EmailMapper {

    /**
     * Maps a {@link EmailCreateDto} to a {@link EmailEntity}.
     *
     * @param dto The {@link EmailCreateDto} to map.
     * @return The resulting {@link EmailEntity}.
     */
    EmailEntity toEntity(EmailCreateDto dto);

    /**
     * Maps a {@link EmailEntity} to a {@link EmailDto}
     *
     * @param entity {@link EmailEntity} to map.
     * @return The resulting {@link EmailDto}.
     */
    EmailDto toDto(EmailEntity entity);

    /**
     * Maps a {@link EmailEntity} to a {@link EmailFullNameDto}
     *
     * @param entity {@link EmailEntity} to map.
     * @return The resulting {@link EmailDto}.
     */
    EmailFullNameDto toEmailMemberDto(EmailEntity entity);
    /**
     * Updates a email object.
     *
     * @param dto The {@link EmailUpdateDto} containing the updates.
     * @param entity The {@link EmailEntity} to be updated.
     * @return The updated {@link EmailEntity}.
     */
    EmailEntity updateEntity(EmailUpdateDto dto, EmailEntity entity);

    /**
     * Maps a list of {@link EmailEntity} to a list of {@link EmailFullNameDto}.
     *
     * @param entityList the {@link List} of {@link EmailEntity} to map.
     * @return A list of {@link EmailFullNameDto}.
     */
    List<EmailDto> toDtoList(List<EmailEntity> entityList);

    /**
     * Maps a page of email entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link EmailEntity} to map.
     * @return A page of {@link EmailDto}
     */
    Page<EmailFullNameDto> toPage(Page<EmailEntity> page);
}
