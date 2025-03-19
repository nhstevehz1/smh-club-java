package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.phone.PhoneCreateDto;
import com.smh.club.api.rest.dto.phone.PhoneDto;
import com.smh.club.api.rest.dto.phone.PhoneFullNameDto;
import com.smh.club.api.rest.dto.phone.PhoneUpdateDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Business logic for phones.
 */
public interface PhoneService {

    /**
     * Retrieves a page of phones from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link PhoneFullNameDto}.
     */
    PagedDto<PhoneFullNameDto> getPage(Pageable pageable);

    /**
     * Retrieves a phone from the database.
     *
     * @param id The id of the phone
     * @return An {@link PhoneDto} type {@link Optional}
     */
    Optional<PhoneDto> getPhone(int id);

    /**
     * Creates a phone and stores it in the database.
     *
     * @param phone The {@link PhoneCreateDto} used to create the phone.
     * @return The newly created phone.
     */
    PhoneDto createPhone(PhoneCreateDto phone);

    /**
     * Updates a phone in the database.
     *
     * @param id The id of the phone to update.
     * @param phone The {@link PhoneUpdateDto} containing the updates.
     * @return The updated {@link PhoneDto}.
     */
    Optional<PhoneDto> updatePhone(int id, PhoneUpdateDto phone);

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
