package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.RenewalModel;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

/**
 * Business logic for renewals.
 */
public interface RenewalService {

    /**
     * Retrieves a page of renewals from the database.
     *
     * @param pageable@return A {@link PagedModel} of type {@link RenewalModel}.
     */
    PagedModel<RenewalModel> getPage(Pageable pageable);

    /**
     * Retrieves a renewal from the database.
     *
     * @param id The id of the renewal
     * @return An {@link RenewalModel} type {@link Optional}
     */
    Optional<RenewalModel> getRenewal(int id);

    /**
     * Creates a renewal and stores it in the database.
     *
     * @param renewal The {@link RenewalModel} used to create the renewal.
     * @return The newly created renewal.
     */
    RenewalModel createRenewal(RenewalModel renewal);

    /**
     * Updates a renewal in the database.
     *
     * @param id The id of the renewal to update.
     * @param renewal The {@link RenewalModel} containing the updates.
     * @return The updated {@link RenewalModel}.
     */
    Optional<RenewalModel> updateRenewal(int id, RenewalModel renewal);

    /**
     * Deletes a renewal from the database.
     *
     * @param id The id of the renewal to delete.
     */
    void deleteRenewal(int id);

    /**
     * Gets a count of the renewal objects in the database.
     *
     * @return The count of renewal objects.
     */
    long getRenewalCount();
}
