package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.models.AddressModel;

import java.util.List;

/**
 * Maps address objects
 */
public interface AddressMapper {

    /**
     * Maps an {@link AddressModel} to an {@link AddressEntity}.
     * @param model The {@link AddressModel} to map.
     * @return The resulting {@link AddressEntity}.
     */
    AddressEntity toEntity(AddressModel model);

    /**
     * Maps an {@link AddressEntity} to an {@link AddressModel}
     * @param entity {@link AddressEntity} to map.
     * @return The resulting {@link AddressModel}.
     */
    AddressModel toModel(AddressEntity entity);

    /**
     * Updates an address object
     * @param model The {@link AddressModel} containing the updates.
     * @param entity The {@link AddressEntity} to be updated.
     * @return The updated {@link AddressEntity}.
     */
    AddressEntity updateEntity(AddressModel model, AddressEntity entity);

    /**
     * Maps a list of {@link AddressEntity} to a list of {@link AddressModel}.
     * @param entityList the {@link List} of {@link AddressEntity} to map.
     * @return A list of {@link AddressModel}.
     */
    List<AddressModel> toModelList(List<AddressEntity> entityList);
}
