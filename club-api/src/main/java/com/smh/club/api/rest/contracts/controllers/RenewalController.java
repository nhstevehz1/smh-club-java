package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
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
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link RenewalDto}.
     */
    ResponseEntity<PageResponse<RenewalDto>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single renewal from the database.
     * @param id The id of the renewal.
     * @return @return A {@link ResponseEntity} containing a {@link RenewalDto}
     */
    ResponseEntity<RenewalDto> get(int id);

    /**
     * Endpoint for getting the total count of addresses in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> get();

    /**
     * Endpoint for creating a renewal.
     * @param renewal The {@link RenewalDto } used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} representing the newly created object.
     */
    ResponseEntity<RenewalDto> create(RenewalDto renewal);

    /**
     * Endpoint for updating a renewal.
     * @param id The id of the renewal to update in the database.
     * @param renewal The {@link RenewalDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} that represents the updated renewal.
     */
    ResponseEntity<RenewalDto> update(int id, RenewalDto renewal);

    /**
     * Endpoint for deleting a renewal from the database.
     * @param id The id of the renewal to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
