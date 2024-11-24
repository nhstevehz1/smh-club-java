package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.rest.dto.AddressDto;

import java.util.List;

/**
 * Maps address objects
 */
public interface AddressMapper {

    /**
     * Maps an {@link AddressDto} to an {@link AddressEntity}.
     * @param dto The {@link AddressDto} to map.
     * @return The resulting {@link AddressEntity}.
     */
    AddressEntity toEntity(AddressDto dto);

    /**
     * Maps an {@link AddressEntity} to an {@link AddressDto}
     * @param entity {@link AddressEntity} to map.
     * @return The resulting {@link AddressDto}.
     */
    AddressDto toDto(AddressEntity entity);

    /**
     * Updates an address object
     * @param dto The {@link AddressDto} containing the updates.
     * @param entity The {@link AddressEntity} to be updated.
     * @return The updated {@link AddressEntity}.
     */
    AddressEntity updateEntity(AddressDto dto, AddressEntity entity);

    /**
     * Maps a list of {@link AddressEntity} to a list of {@link AddressDto}.
     * @param entityList the {@link List} of {@link AddressEntity} to map.
     * @return A list of {@link AddressDto}.
     */
    List<AddressDto> toDtoList(List<AddressEntity> entityList);
}
