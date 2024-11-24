package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
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
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link MemberModel}.
     */
    ResponseEntity<PagedModel<MemberModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single member from the database.
     * @param id The id of the member.
     * @return @return A {@link ResponseEntity} containing a {@link MemberModel}
     */
    ResponseEntity<MemberModel> get(int id);

    /**
     * Endpoint for getting the total count of memberes in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating an member.
     * @param member The {@link MemberModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link MemberModel} representing the newly created object.
     */
    ResponseEntity<MemberModel> create(MemberModel member);

    /**
     * Endpoint for updating an member.
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link MemberModel} that represents the updated member.
     */
    ResponseEntity<MemberModel> update(int id, MemberModel member);

    /**
     * Endpoint for deleting an member from the database.
     * @param id The id of the member to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);

}
