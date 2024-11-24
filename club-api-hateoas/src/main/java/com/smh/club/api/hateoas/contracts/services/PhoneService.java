package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.PhoneModel;
import org.springframework.hateoas.PagedModel;

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
     * @return A {@link PagedModel} of type {@link PhoneModel}.
     */
    PagedModel<PhoneModel> getPhoneListPage(int pageNumber, int pageSize, String direction, String sort);

    /**
     * Retrieves a phone from the database.
     * @param id The id of the phone
     * @return An {@link PhoneModel} type {@link Optional}
     */
    Optional<PhoneModel> getPhone(int id);

    /**
     * Creates a phone and stores it in the database.
     * @param phone The {@link PhoneModel} used to create the phone.
     * @return The newly created phone.
     */
    PhoneModel createPhone(PhoneModel phone);

    /**
     * Updates a phone int he database.
     * @param id The id of the phone to update.
     * @param phone The {@link PhoneModel} containing the updates.
     * @return The updated {@link PhoneModel}.
     */
    Optional<PhoneModel> updatePhone(int id, PhoneModel phone);

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
