package com.smh.club.api.data.domain.repos;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A repository for {@link EmailEntity} objects
 */
@Repository
public interface MembersRepo extends JpaRepository<MemberEntity, Integer> {

    /**
     * Returns true if a member number is already in use.  Otherwise false
     */
    boolean existsByMemberNumber(int memberNumber);

    List<MemberNumberOnly> findByMemberNumberGreaterThanEqualAndMemberNumberLessThanEqual(int low, int high);

    /**
     * Finds a member object by member number.
     * @param memberNumber The member number of the member object.
     * @return An {@link Optional} of type {@link MemberEntity}
     */
    Optional<MemberEntity> findByMemberNumber(int memberNumber);
}
