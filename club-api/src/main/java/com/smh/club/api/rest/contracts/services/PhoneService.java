package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.PhoneDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Business logic for phonees.
 */
public interface PhoneService {

    /**
     * Retrieves a page of phonees from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param direction The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link Page} of type {@link PhoneDto}.
     */
    Page<PhoneDto> getPhoneListPage(int pageSize, int pageNumber, String direction, String sort);

    /**
     * Retrieves a phone from the database.
     * @param id The id of the phone
     * @return An {@link PhoneDto} type {@link Optional}
     */
    Optional<PhoneDto> getPhone(int id);

    /**
     * Creates a phone and stores it in the database.
     * @param phone The {@link PhoneDto} used to create the phone.
     * @return The newly created phone.
     */
    PhoneDto createPhone(PhoneDto phone);

    /**
     * Updates a phone int he database.
     * @param id The id of the phone to update.
     * @param phone The {@link PhoneDto} containing the updates.
     * @return The updated {@link PhoneDto}.
     */
    Optional<PhoneDto> updatePhone(int id, PhoneDto phone);

    /**
     * Deletes a phone from the database.
     * @param id The id of the phone to delete.
     */
    void deletePhone(int id);

    /**
     * Gets a count of the phone objects in the database.
     * @return The count of phone objects.
     */
    long getPhoneCount();
}
