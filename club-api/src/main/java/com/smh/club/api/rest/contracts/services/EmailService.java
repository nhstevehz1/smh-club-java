package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.EmailDto;
import org.springframework.data.domain.Page;

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
     * @return A {@link Page} of type {@link EmailDto}.
     */
    Page<EmailDto> getEmailListPage(int pageNumber, int pageSize, String direction, String sort);

    /**
     * Retrieves an email from the database.
     * @param id The id of the email
     * @return An {@link EmailDto} type {@link Optional}
     */
    Optional<EmailDto> getEmail(int id);

    /**
     * Creates an email and stores it in the database.
     * @param email The {@link EmailDto} used to create the email.
     * @return The newly created email.
     */
    EmailDto createEmail(EmailDto email);

    /**
     * Updates an email int he database.
     * @param id The id of the email to update.
     * @param email The {@link EmailDto} containing the updates.
     * @return The updated {@link EmailDto}.
     */
    Optional<EmailDto> updateEmail(int id, EmailDto email);

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
