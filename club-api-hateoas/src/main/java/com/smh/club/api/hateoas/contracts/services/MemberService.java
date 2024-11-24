package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.MemberModel;
import org.springframework.hateoas.PagedModel;

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
     * @return A {@link PagedModel} of type {@link MemberModel}.
     */ 
    PagedModel<MemberModel> getMemberListPage(int pageSize, int pageNumber, String direction, String sort);

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
     * Updates a member int he database.
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
