package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.AddressModel;
import org.springframework.hateoas.PagedModel;

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
     * @return A {@link PagedModel} of type {@link AddressModel}.
     */
    PagedModel<AddressModel> getAddressListPage(int pageNumber, int pageSize, String direction, String sort);

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
     * Updates an address int he database.
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
