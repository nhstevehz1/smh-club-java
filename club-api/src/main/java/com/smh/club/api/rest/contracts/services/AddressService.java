package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Business logic for addresses.
 */
public interface AddressService {

    /**
     * Retrieves a page of addresses from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param direction The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link Page} of type {@link AddressDto}.
     */
    Page<AddressDto> getAddressListPage(int pageNumber, int pageSize,
                                        @NonNull String direction, @NonNull String sort);

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
    AddressDto createAddress(AddressDto address);

    /**
     * Updates an address int he database.
     * @param id The id of the address to update.
     * @param address The {@link AddressDto} containing the updates.
     * @return The updated {@link AddressDto}.
     */
    Optional<AddressDto> updateAddress(int id, AddressDto address);

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
