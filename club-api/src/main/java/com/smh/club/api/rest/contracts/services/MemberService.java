package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Business logic for members.
 */
public interface MemberService {

    /**
     * Retrieves a page of members from the database.
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param direction The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link Page} of type {@link MemberDto}.
     */
    Page<MemberDto> getMemberListPage(int pageSize, int pageNumber, String direction, String sort);

    /**
     * Retrieves a member from the database.
     * @param id The id of the member
     * @return An {@link MemberDto} type {@link Optional}
     */
    Optional<MemberDto> getMember(int id);

    /**
     * Creates a member and stores it in the database.
     * @param member The {@link MemberDto} used to create the member.
     * @return The newly created member.
     */
    MemberDto createMember(MemberDto member);

    /**
     * Updates a member int he database.
     * @param id The id of the member to update.
     * @param member The {@link MemberDto} containing the updates.
     * @return The updated {@link MemberDto}.
     */
    Optional<MemberDto> updateMember(int id, MemberDto member);

    /**
     * Deletes a member from the database.
     * @param id The id of the member to delete.
     */
    void deleteMember(int id);

    /**
     * Gets a count of the member objects in the database.
     * @return The count of member objects.
     */
    long getMemberCount();
    Optional<MemberDetailDto> getMemberDetail(int id);
}
