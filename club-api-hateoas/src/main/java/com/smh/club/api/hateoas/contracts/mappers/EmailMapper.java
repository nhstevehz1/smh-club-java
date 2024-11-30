package com.smh.club.api.hateoas.contracts.mappers;

import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.hateoas.models.EmailModel;
import java.util.List;

/**
 * Maps email objects
 */
public interface EmailMapper {

    /**
     * Maps a {@link EmailModel} to a {@link EmailEntity}.
     * @param model The {@link EmailModel} to map.
     * @return The resulting {@link EmailEntity}.
     */
    EmailEntity toEntity(EmailModel model);

    /**
     * Maps a {@link EmailEntity} to a {@link EmailModel}
     * @param entity {@link EmailEntity} to map.
     * @return The resulting {@link EmailModel}.
     */
    EmailModel toModel(EmailEntity entity);
    
    /**
     * Updates a email object
     * @param model The {@link EmailModel} containing the updates. 
     * @param entity The {@link EmailEntity} to be updated.
     * @return The updated {@link EmailEntity}.
     */
    EmailEntity updateEntity(EmailModel model, EmailEntity entity);

    /**
     * Maps a list of {@link EmailEntity} to a list of {@link EmailModel}.
     * @param entityList the {@link List} of {@link EmailEntity} to map.
     * @return A list of {@link EmailModel}.
     */
    List<EmailModel> toModelList(List<EmailEntity> entityList);
}
