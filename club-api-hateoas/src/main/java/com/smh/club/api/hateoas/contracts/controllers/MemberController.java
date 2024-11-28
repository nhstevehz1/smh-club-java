package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import smh.club.shared.annotations.SortConstraint;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
@Validated
public interface MemberController {

    /**
     * Endpoint for retrieving a page of member objects from the database.
     *
     * @param pageable A {@link Pageable} that contains page and sort information.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link MemberModel}.
     */
    @Valid
    ResponseEntity<PagedModel<MemberModel>> page(
        @SortConstraint(dtoClass = PagedModel.class) Pageable pageable);

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
     * Endpoint for creating a member.
     * @param member The {@link MemberModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link MemberModel} representing the newly created object.
     */
    ResponseEntity<MemberModel> create(MemberModel member);

    /**
     * Endpoint for updating a member.
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link MemberModel} that represents the updated member.
     */
    ResponseEntity<MemberModel> update(int id, MemberModel member);

    /**
     * Endpoint for deleting a member from the database.
     * @param id The id of the member to delete
     * @return ab empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);

}
