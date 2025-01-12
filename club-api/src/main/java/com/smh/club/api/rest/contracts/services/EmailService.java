package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.EmailDto;
import com.smh.club.api.rest.dto.EmailMemberDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Business logic for emails.
 */
public interface EmailService {

    /**
     * Retrieves a page of emails from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link EmailMemberDto}.
     */
    PagedDto<EmailMemberDto> getPage(Pageable pageable);

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
