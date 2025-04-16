package com.smh.club.api.contracts.services;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.email.EmailMemberDto;
import com.smh.club.api.response.PagedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
     * Finds all email addresses belonging to member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link EmailDto}'s.
     */
    List<EmailDto> findAllByMemberId(int memberId);

    /**
     * Retrieves an email from the database.
     *
     * @param id The id of the email
     * @return An {@link EmailDto} type {@link Optional}
     */
    Optional<EmailDto> getEmail(int id);

    /**
     * Creates an email and stores it in the database.
     *
     * @param email The {@link EmailDto} used to create the email.
     * @return The newly created email.
     */
    EmailDto createEmail(EmailDto email);

    /**
     * Updates an email int he database.
     *
     * @param id The id of the email to update.
     * @param email The {@link EmailDto} containing the updates.
     * @return The updated {@link EmailDto}.
     */
    Optional<EmailDto> updateEmail(int id, EmailDto email);

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
