package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.EmailModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

/**
 * Defines REST endpoints that targets email objects in the database.
 */
public interface EmailController {

    /**
     * Endpoint for retrieving a page of email objects from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link EmailModel}.
     */
    ResponseEntity<PagedModel<EmailModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    /**
     * Endpoint for retrieving a single email from the database.
     * @param id The id of the email.
     * @return @return A {@link ResponseEntity} containing a {@link EmailModel}
     */
    ResponseEntity<EmailModel> get(int id);

    /**
     * Endpoint for getting the total count of emailes in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    ResponseEntity<CountResponse> count();

    /**
     * Endpoint for creating an email.
     * @param email The {@link EmailModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link EmailModel} representing the newly created object.
     */
    ResponseEntity<EmailModel> create(EmailModel email);

    /**
     * Endpoint for updating an email.
     * @param id The id of the email to update in the database.
     * @param email The {@link EmailModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link EmailModel} that represents the updated email.
     */
    ResponseEntity<EmailModel> update(int id, EmailModel email);

    /**
     * Endpoint for deleting an email from the database.
     * @param id The id of the email to delete
     * @return an empty {@link ResponseEntity}.
     */
    ResponseEntity<Void> delete(int id);
}
