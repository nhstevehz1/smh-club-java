package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.member.MemberCreateDto;
import com.smh.club.api.rest.dto.member.MemberDetailDto;
import com.smh.club.api.rest.dto.member.MemberDto;
import com.smh.club.api.rest.dto.member.MemberUpdateDto;
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
     *
     * @param id The id of the member
     * @return An {@link MemberDto} type {@link Optional}
     */
    Optional<MemberDto> getMember(int id);

    /**
     * Creates a member and stores it in the database.
     *
     * @param member The {@link MemberCreateDto} used to create the member.
     * @return The newly created {@link MemberDto}.
     */
    MemberDto createMember(MemberCreateDto member);

    /**
     * Updates a member in the database.
     *
     * @param id     The id of the member to update.
     * @param member The {@link MemberUpdateDto} containing the updates.
     * @return The updated {@link MemberDto}.
     */
    Optional<MemberDto> updateMember(int id, MemberUpdateDto member);

    /**
     * Deletes a member from the database.
     *
     * @param id The id of the member to delete.
     */
    void deleteMember(int id);

    /**
     * Gets a count of the member objects in the database.
     *
     * @return The count of member objects.
     */
    long getMemberCount();

    /**
     * Gets a member that includes children.
     *
     * @param id The member id.
     * @return A {@link MemberDetailDto} object.
     */
    Optional<MemberDetailDto> getMemberDetail(int id);
}
