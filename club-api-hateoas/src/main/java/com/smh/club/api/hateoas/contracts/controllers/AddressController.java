package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets address objects in the database.
 */
public interface AddressController {

    /**
     * Endpoint for retrieving a page of address objects from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link AddressModel}.
     */
    ResponseEntity<PagedModel<AddressModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single address from the database.
     * @param id The id of the address.
     * @return @return A {@link ResponseEntity} containing a {@link AddressModel}
     */
    ResponseEntity<AddressModel> get(int id);

    /**
     * Endpoint for getting the total count of addresses in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating an address.
     * @param address The {@link AddressModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link AddressModel} representing the newly created object.
     */
    ResponseEntity<AddressModel> create(AddressModel address);

    /**
     * Endpoint for updating an address.
     * @param id The id of the address to update in the database.
     * @param address The {@link AddressModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link AddressModel} that represents the updated address.
     */
    ResponseEntity<AddressModel> update(int id, AddressModel address);

    /**
     * Endpoint for deleting an address from the database.
     * @param id The id of the address to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
