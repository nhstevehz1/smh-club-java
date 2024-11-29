package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.EmailModel;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

/**
 * Business logic for emails.
 */
public interface EmailService {

    /**
     * Retrieves a page of emails from the database.
     *
     * @param pageable A {@link Pageable} that contains the sort criteria.
     * @return A {@link PagedModel} of type {@link EmailModel}.
     */
    PagedModel<EmailModel> getPage(Pageable pageable);

    /**
     * Retrieves an email from the database.
     *
     * @param id The id of the email
     * @return An {@link EmailModel} type {@link Optional}
     */
    Optional<EmailModel> getEmail(int id);

    /**
     * Creates an email and stores it in the database.
     *
     * @param email The {@link EmailModel} used to create the email.
     * @return The newly created email.
     */
    EmailModel createEmail(EmailModel email);

    /**
     * Updates an email int he database.
     *
     * @param id The id of the email to update.
     * @param email The {@link EmailModel} containing the updates.
     * @return The updated {@link EmailModel}.
     */
    Optional<EmailModel> updateEmail(int id, EmailModel email);

    /**
     * Deletes an email from the database.
     *
     * @param id The id of the email to delete.
     */
    void deleteEmail(int id);

    /**
     * Gets a count of the email objects in the database.
     *
     * @return The count of email objects.
     */
    long getEmailCount();
}
