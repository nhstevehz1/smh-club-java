package com.smh.club.api.contracts.services;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalMemberDto;
import com.smh.club.api.response.PagedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for renewals.
 */
public interface RenewalService {

    /**
     * Retrieves a page of renewals from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link RenewalMemberDto}.
     */
    PagedDto<RenewalMemberDto> getPage(Pageable pageable);

    /**
     * Finds all renewals belonging to member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link RenewalDto}'s.
     */
    List<RenewalDto> findAllByMemberId(int memberId);

    /**
     * Retrieves a renewal from the database.
     *
     * @param id The id of the renewal
     * @return An {@link RenewalDto} type {@link Optional}
     */
    Optional<RenewalDto> getRenewal(int id);

    /**
     * Creates a renewal and stores it in the database.
     *
     * @param renewal The {@link RenewalDto} used to create the renewal.
     * @return The newly created renewal.
     */
    RenewalDto createRenewal(RenewalDto renewal);

    /**
     * Updates a renewal in the database.
     *
     * @param id The id of the renewal to update.
     * @param renewal The {@link RenewalDto} containing the updates.
     * @return The updated {@link RenewalDto}.
     */
    Optional<RenewalDto> updateRenewal(int id, RenewalDto renewal);

    /**
     * Deletes a renewal from the database.
     *
     * @param id The id of the renewal to delete.
     */
    void deleteRenewal(int id);

    /**
     * Gets a count of the renewal objects in the database.
     *
     * @return The count of renewal objects.
     */
    long getRenewalCount();
}
