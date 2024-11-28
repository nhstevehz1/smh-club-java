package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.PhoneModel;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

/**
 * Business logic for phonees.
 */
public interface PhoneService {

    /**
     * Retrieves a page of phones from the database.
     *
     * @param pageable A {@link Pageable} that contains the sort criteria.
     * @return A {@link PagedModel} of type {@link PhoneModel}.
     */
    PagedModel<PhoneModel> getPage(Pageable pageable);

    /**
     * Retrieves a phone from the database.
     *
     * @param id The id of the phone
     * @return An {@link PhoneModel} type {@link Optional}
     */
    Optional<PhoneModel> getPhone(int id);

    /**
     * Creates a phone and stores it in the database.
     *
     * @param phone The {@link PhoneModel} used to create the phone.
     * @return The newly created phone.
     */
    PhoneModel createPhone(PhoneModel phone);

    /**
     * Updates a phone int he database.
     *
     * @param id The id of the phone to update.
     * @param phone The {@link PhoneModel} containing the updates.
     * @return The updated {@link PhoneModel}.
     */
    Optional<PhoneModel> updatePhone(int id, PhoneModel phone);

    /**
     * Deletes a phone from the database.
     *
     * @param id The id of the phone to delete.
     */
    void deletePhone(int id);

    /**
     * Gets a count of the phone objects in the database.
     *
     * @return The count of phone objects.
     */
    long getPhoneCount();
}
