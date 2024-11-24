package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.RenewalDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Business logic for renewals.
 */
public interface RenewalService {

    /**
     * Retrieves a page of renewals from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param direction The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link Page} of type {@link RenewalDto}.
     */
    Page<RenewalDto> getRenewalListPage(int pageSize, int pageNumber, String direction, String sort);

    /**
     * Retrieves a renewal from the database.
     * @param id The id of the renewal
     * @return An {@link RenewalDto} type {@link Optional}
     */
    Optional<RenewalDto> getRenewal(int id);

    /**
     * Creates a renewal and stores it in the database.
     * @param renewal The {@link RenewalDto} used to create the renewal.
     * @return The newly created renewal.
     */
    RenewalDto createRenewal(RenewalDto renewal);

    /**
     * Updates a renewal int he database.
     * @param id The id of the renewal to update.
     * @param renewal The {@link RenewalDto} containing the updates.
     * @return The updated {@link RenewalDto}.
     */
    Optional<RenewalDto> updateRenewal(int id, RenewalDto renewal);

    /**
     * Deletes a renewal from the database.
     * @param id The id of the renewal to delete.
     */
    void deleteRenewal(int id);

    /**
     * Gets a count of the renewal objects in the database.
     * @return The count of renewal objects.
     */
    long getRenewalCount();
}
