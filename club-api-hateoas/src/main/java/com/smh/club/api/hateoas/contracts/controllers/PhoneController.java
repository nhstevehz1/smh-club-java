package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.PhoneModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets phone objects in the database.
 */
public interface PhoneController {

    /**
     * Endpoint for retrieving a page of phone objects from the database.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link PhoneModel}.
     */
    ResponseEntity<PagedModel<PhoneModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single phone from the database.
     * @param id The id of the phone.
     * @return @return A {@link ResponseEntity} containing a {@link PhoneModel}
     */
    ResponseEntity<PhoneModel> get(int id);

    /**
     * Endpoint for getting the total count of phonees in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating a phone.
     * @param phone The {@link PhoneModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link PhoneModel} representing the newly created object.
     */
    ResponseEntity<PhoneModel> create(PhoneModel phone);

    /**
     * Endpoint for updating a phone.
     * @param id The id of the phone to update in the database.
     * @param phone The {@link PhoneModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link PhoneModel} that represents the updated phone.
     */
    ResponseEntity<PhoneModel> update(int id, PhoneModel phone);

    /**
     * Endpoint for deleting a phone from the database.
     * @param id The id of the phone to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
