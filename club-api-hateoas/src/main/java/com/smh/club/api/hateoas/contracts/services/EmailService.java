package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.EmailModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

/**
 * Business logic for emails.
 */
public interface EmailService {

    /**
     * Retrieves a page of emailes from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param direction The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link PagedModel} of type {@link EmailModel}.
     */
    PagedModel<EmailModel> getEmailListPage(int pageNumber, int pageSize, String direction, String sort);

    /**
     * Retrieves an email from the database.
     * @param id The id of the email
     * @return An {@link EmailModel} type {@link Optional}
     */
    Optional<EmailModel> getEmail(int id);

    /**
     * Creates an email and stores it in the database.
     * @param email The {@link EmailModel} used to create the email.
     * @return The newly created email.
     */
    EmailModel createEmail(EmailModel email);

    /**
     * Updates an email int he database.
     * @param id The id of the email to update.
     * @param email The {@link EmailModel} containing the updates.
     * @return The updated {@link EmailModel}.
     */
    Optional<EmailModel> updateEmail(int id, EmailModel email);

    /**
     * Deletes an email from the database.
     * @param id The id of the email to delete.
     */
    void deleteEmail(int id);

    /**
     * Gets a count of the email objects in the database.
     * @return The count of email objects.
     */
    long getEmailCount();
}
