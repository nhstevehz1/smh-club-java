package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.dto.AddressDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps address objects
 */
public interface AddressMapper {

    /**
     * Maps an {@link AddressDto} to an {@link AddressEntity}.
     *
     * @param dto The {@link AddressDto} to map.
     * @return The resulting {@link AddressEntity}.
     */
    AddressEntity toEntity(AddressDto dto);

    /**
     * Maps an {@link AddressEntity} to an {@link AddressDto}
     *
     * @param entity {@link AddressEntity} to map.
     * @return The resulting {@link AddressDto}.
     */
    AddressDto toDto(AddressEntity entity);

    /**
     * Updates an address object
     *
     * @param dto The {@link AddressDto} containing the updates.
     * @param entity The {@link AddressEntity} to be updated.
     * @return The updated {@link AddressEntity}.
     */
    AddressEntity updateEntity(AddressDto dto, AddressEntity entity);

    /**
     * Maps a list of {@link AddressEntity} to a list of {@link AddressDto}.
     *
     * @param entityList The {@link List} of {@link AddressEntity} to map.
     * @return A list of {@link AddressDto}.
     */
    List<AddressDto> toDtoList(List<AddressEntity> entityList);

    /**
     * Maps a page of address entities to a page of DTOs.
     *
     * @param page The {@link Page} of {@link AddressEntity} to map.
     * @return A page of {@link AddressDto}
     */
    Page<AddressDto> toPage(Page<AddressEntity> page);
}
