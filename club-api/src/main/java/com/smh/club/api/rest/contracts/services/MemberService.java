package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.MemberCreateDto;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Business logic for members.
 */
public interface MemberService {

    /**
     * Retrieves a page of members from the database.
     *
     * @param pageable@return A {@link PagedDto} of type {@link MemberDto}.
     */
    PagedDto<MemberDto> getPage(Pageable pageable);

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
    MemberDto createMember(MemberCreateDto member);

    /**
     * Updates a member in the database.
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
