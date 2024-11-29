package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.MemberModel;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

/**
 * Business logic for members.
 */
public interface MemberService {

    /**
     * Retrieves a page of members from the database.
     *
     * @param pageable A {@link Pageable} that contains page and sort information.
     * @return A {@link PagedModel} of type {@link MemberModel}.
     */
    PagedModel<MemberModel> getPage(Pageable pageable);


    /**
     * Retrieves a member from the database.
     * @param id The id of the member
     * @return An {@link MemberModel} type {@link Optional}
     */
    Optional<MemberModel> getMember(int id);

    /**
     * Creates a member and stores it in the database.
     * @param member The {@link MemberModel} used to create the member.
     * @return The newly created member.
     */
    MemberModel createMember(MemberModel member);

    /**
     * Updates a member in the database.
     * @param id The id of the member to update.
     * @param member The {@link MemberModel} containing the updates.
     * @return The updated {@link MemberModel}.
     */
    Optional<MemberModel> updateMember(int id, MemberModel member);

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
}
