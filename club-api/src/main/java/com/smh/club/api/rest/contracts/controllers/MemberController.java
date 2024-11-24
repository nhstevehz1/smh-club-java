package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
public interface MemberController {

    /**
     * Endpoint for retrieving a page of member objects from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link MemberDto}.
     */
    ResponseEntity<PageResponse<MemberDto>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single member from the database.
     * @param id The id of the member.
     * @return @return A {@link ResponseEntity} containing a {@link MemberDto}
     */
    ResponseEntity<MemberDto> get(int id);

    /**
     * Endpoint for getting the total count of memberes in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating a member.
     * @param member The {@link MemberDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link MemberDto} representing the newly created object.
     */
    ResponseEntity<MemberDto> create(MemberDto member);

    /**
     * Endpoint for updating a member.
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link MemberDto} that represents the updated member.
     */
    ResponseEntity<MemberDto> update(int id, MemberDto member);

    /**
     * Endpoint for deleting a member from the database.
     * @param id The id of the member to delete
     * @return ab empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
    
    ResponseEntity<MemberDetailDto> detail(int id);
}
