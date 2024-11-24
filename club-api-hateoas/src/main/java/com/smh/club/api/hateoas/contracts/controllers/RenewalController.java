package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets renewal objects in the database.
 */
public interface RenewalController {

    /**
     * Endpoint for retrieving a page of renewal objects from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link RenewalModel}.
     */
    ResponseEntity<PagedModel<RenewalModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single renewal from the database.
     * @param id The id of the renewal.
     * @return @return A {@link ResponseEntity} containing a {@link RenewalModel}
     */
    ResponseEntity<RenewalModel> get(int id);

    /**
     * Endpoint for getting the total count of addresses in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating an address.
     * @param renewal The {@link RenewalModel } used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link RenewalModel} representing the newly created object.
     */
    ResponseEntity<RenewalModel> create(RenewalModel renewal);
    
    /**
     * Endpoint for updating an renewal.
     * @param id The id of the renewal to update in the database.
     * @param renewal The {@link RenewalModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link RenewalModel} that represents the updated renewal.
     */
    ResponseEntity<RenewalModel> update(int id, RenewalModel renewal);
    
    /**
     * Endpoint for deleting an renewal from the database.
     * @param id The id of the renewal to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
