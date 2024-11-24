package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.dto.PhoneDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets phone objects in the database.
 */
public interface PhoneController {

    /**
     * Endpoint for retrieving a page of phone objects from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link PhoneDto}.
     */
    ResponseEntity<PageResponse<PhoneDto>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single phone from the database.
     * @param id The id of the phone.
     * @return @return A {@link ResponseEntity} containing a {@link PhoneDto}
     */
    ResponseEntity<PhoneDto> get(int id);

    /**
     * Endpoint for getting the total count of phonees in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating a phone.
     * @param phone The {@link PhoneDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} representing the newly created object.
     */
    ResponseEntity<PhoneDto> create(PhoneDto phone);

    /**
     * Endpoint for updating a phone.
     * @param id The id of the phone to update in the database.
     * @param phone The {@link PhoneDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} that represents the updated phone.
     */
    ResponseEntity<PhoneDto> update(int id, PhoneDto phone);

    /**
     * Endpoint for deleting a phone from the database.
     * @param id The id of the phone to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
