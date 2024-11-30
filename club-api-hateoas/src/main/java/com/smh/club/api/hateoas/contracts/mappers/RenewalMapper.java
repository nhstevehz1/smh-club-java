package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.hateoas.models.RenewalModel;
import java.util.List;

/**
 * Maps renewal objects.
 */
public interface RenewalMapper {

    /**
     * Maps a {@link RenewalModel} to a {@link RenewalEntity}.
     * @param model The {@link RenewalModel} to map.
     * @return The resulting {@link RenewalEntity}.
     */
    RenewalEntity toEntity(RenewalModel model);

    /**
     * Maps a {@link RenewalEntity} to a {@link RenewalModel}
     * @param entity {@link RenewalEntity} to map.
     * @return The resulting {@link RenewalModel}.
     */
    RenewalModel toModel(RenewalEntity entity);

    /**
     * Updates a renewal object
     * @param model The {@link RenewalModel} containing the updates. 
     * @param entity The {@link RenewalEntity} to be updated.
     * @return The updated {@link RenewalEntity}.
     */
    RenewalEntity updateEntity(RenewalModel model, RenewalEntity entity);

    /**
     * Maps a list of {@link RenewalEntity} to a list of {@link RenewalModel}.
     * @param entityList the {@link List} of {@link RenewalEntity} to map.
     * @return A list of {@link RenewalModel}.
     */
    List<RenewalModel> toModelList(List<RenewalEntity> entityList);
}
