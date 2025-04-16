package com.smh.club.api.contracts.services;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneMemberDto;
import com.smh.club.api.response.PagedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for phones.
 */
public interface PhoneService {

    /**
     * Retrieves a page of phones from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link PhoneMemberDto}.
     */
    PagedDto<PhoneMemberDto> getPage(Pageable pageable);

    /**
     * Finds all phone numbers belonging to member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link PhoneDto}'s.
     */
    List<PhoneDto> findAllByMemberId(int memberId);

    /**
     * Retrieves a phone from the database.
     *
     * @param id The id of the phone
     * @return An {@link PhoneDto} type {@link Optional}
     */
    Optional<PhoneDto> getPhone(int id);

    /**
     * Creates a phone and stores it in the database.
     *
     * @param phone The {@link PhoneDto} used to create the phone.
     * @return The newly created phone.
     */
    PhoneDto createPhone(PhoneDto phone);

    /**
     * Updates a phone in the database.
     *
     * @param id The id of the phone to update.
     * @param phone The {@link PhoneDto} containing the updates.
     * @return The updated {@link PhoneDto}.
     */
    Optional<PhoneDto> updatePhone(int id, PhoneDto phone);

    /**
     * Deletes a phone from the database.
     * @param id The id of the phone to delete.
     */
    void deletePhone(int id);

    /**
     * Gets a count of the phone objects in the database.
     * @return The count of phone objects.
     */
    long getPhoneCount();
}
