package com.smh.club.api.contracts.mappers;

import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.dto.address.AddressCreateDto;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressFullNameDto;
import com.smh.club.api.dto.address.AddressUpdateDto;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Maps address objects
 */
public interface AddressMapper {

    /**
     * Maps an {@link AddressCreateDto} to an {@link AddressEntity}.
     *
     * @param dto The {@link AddressCreateDto} to map.
     * @return The resulting {@link AddressEntity}.
     */
    AddressEntity toEntity(AddressCreateDto dto);


    /**
     * Maps an {@link AddressEntity} to an {@link AddressDto}
     *
     * @param entity {@link AddressEntity} to map.
     * @return The resulting {@link AddressDto}.
     */
    AddressDto toDto(AddressEntity entity);

    /**
     * Maps an {@link AddressEntity} to an {@link AddressFullNameDto}
     *      *
     *      * @param entity {@link AddressEntity} to map.
     *      * @return The resulting {@link AddressFullNameDto}.
     */
    AddressFullNameDto toAddressMemberDto(AddressEntity entity);

    /**
     * Updates an address object
     *
     * @param dto The {@link AddressDto} containing the updates.
     * @param entity The {@link AddressEntity} to be updated.
     * @return The updated {@link AddressEntity}.
     */
    AddressEntity updateEntity(AddressUpdateDto dto, AddressEntity entity);

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
     * @return A page of {@link AddressFullNameDto}
     */
    Page<AddressFullNameDto> toPage(Page<AddressEntity> page);
}
