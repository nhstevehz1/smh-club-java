package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.dto.AddressDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
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
     * @return A {@link ResponseEntity} containing an {@link AddressDto}.
     */
    ResponseEntity<PageResponse<AddressDto>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single address from the database.
     * @param id The id of the address.
     * @return @return A {@link ResponseEntity} containing a {@link AddressDto}
     */
    ResponseEntity<AddressDto> get(int id);


    /**
     * Endpoint for getting the total count of addresses in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating an address.
     * @param address The {@link AddressDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link AddressDto} representing the newly created object.
     */
    ResponseEntity<AddressDto> create(AddressDto address);
    
    /**
     * Endpoint for updating an address.
     * @param id The id of the address to update in the database.
     * @param address The {@link AddressDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link AddressDto} that represents the updated address.
     */
    ResponseEntity<AddressDto> update(int id, AddressDto address);

    /**
     * Endpoint for deleting an address from the database.
     * @param id The id of the address to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
