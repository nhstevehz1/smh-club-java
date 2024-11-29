package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.AddressModel;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

/**
 * Business logic for addresses.
 */
public interface AddressService {

    /**
     * Retrieves a page of addresses from the database.
     * @
     * @param pageable The {@link Pageable} that contains the sort criteria.
     * @return A {@link PagedModel} of type {@link AddressModel}.
     */
    PagedModel<AddressModel> getPage(Pageable pageable);

    /**
     * Retrieves an address from the database.
     * @param id The id of the address
     * @return An {@link AddressModel} type {@link Optional}
     */
    Optional<AddressModel> getAddress(int id);

    /**
     * Creates an address and stores it in the database.
     * @param address The {@link AddressModel} used to create the address.
     * @return The newly created address.
     */
    AddressModel createAddress(AddressModel address);

    /**
     * Updates an address in the database.
     * @param id The id of the address to update.
     * @param address The {@link AddressModel} containing the updates.
     * @return The updated {@link AddressModel}.
     */
    Optional<AddressModel> updateAddress(int id, AddressModel address);

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
