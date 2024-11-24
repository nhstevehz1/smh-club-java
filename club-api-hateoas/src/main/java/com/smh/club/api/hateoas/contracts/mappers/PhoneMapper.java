package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.models.PhoneModel;

import java.util.List;

/**
 * Maps phone objects
 */
public interface PhoneMapper {

    /**
     * Maps a {@link PhoneModel} to a {@link PhoneEntity}.
     * @param model The {@link PhoneModel} to map.
     * @return The resulting {@link PhoneEntity}.
     */
    PhoneEntity toEntity(PhoneModel model);

    /**
     * Maps a {@link PhoneEntity} to a {@link PhoneModel}
     * @param entity {@link PhoneEntity} to map.
     * @return The resulting {@link PhoneModel}.
     */
    PhoneModel toModel(PhoneEntity entity);

    /**
     * Updates a phone object
     * @param model The {@link PhoneModel} containing the updates. 
     * @param entity The {@link PhoneEntity} to be updated.
     * @return The updated {@link PhoneEntity}.
     */
    PhoneEntity updateEntity(PhoneModel model, PhoneEntity entity);

    /**
     * Maps a list of {@link PhoneEntity} to a list of {@link PhoneModel}.
     * @param entityList the {@link List} of {@link PhoneEntity} to map.
     * @return A list of {@link PhoneModel}.
     */
    List<PhoneModel> toModelList(List<PhoneEntity> entityList);
}
