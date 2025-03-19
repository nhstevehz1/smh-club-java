package com.smh.club.api.contracts.services;

import com.smh.club.api.dto.address.AddressCreateDto;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressFullNameDto;
import com.smh.club.api.dto.address.AddressUpdateDto;
import com.smh.club.api.response.PagedDto;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Business logic for addresses.
 */
public interface AddressService {

    /**
     * Retrieves a page of addresses from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link AddressFullNameDto}.
     */
    PagedDto<AddressFullNameDto> getPage(Pageable pageable);

    /**
     * Retrieves an address from the database.
     * @param id The id of the address
     * @return An {@link AddressDto} type {@link Optional}
     */
    Optional<AddressDto> getAddress(int id);

    /**
     * Creates an address and stores it in the database.
     * @param address The {@link AddressDto} used to create the address.
     * @return The newly created address.
     */
    AddressDto createAddress(AddressCreateDto address);

    /**
     * Updates an address in the database.
     *
     * @param id The id of the address to update.
     * @param address The {@link AddressDto} containing the updates.
     * @return The updated {@link AddressDto}.
     */
    Optional<AddressDto> updateAddress(int id, AddressUpdateDto address);

    /**
     * Deletes an address from the database.
     * @param id The id of the address to delete.
     */
    void deleteAddress(int id);

    /**
     * Gets a count of the address objects in the database.
     * @return The count of address objects.
     */
    long getAddressCount();
}
