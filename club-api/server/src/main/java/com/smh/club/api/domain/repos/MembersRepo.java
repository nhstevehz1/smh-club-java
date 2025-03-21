package com.smh.club.api.domain.repos;

import com.smh.club.api.domain.entities.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A repository for {@link MemberEntity} objects
 */
@Repository
public interface MembersRepo extends JpaRepository<MemberEntity, Integer> {

    /**
     * Returns the last used member number before a gap in the sequence.
     * ex: exiting member numbers equal {1,2,3,5,6,7,8} where 4 is missing.  3 is returned.
     *
     * @return An optional Integer.  Returns an empty optional if there are no records in th table.
     */
    @Query(
    "SELECT MIN(m1.memberNumber + 1) from MemberEntity m1 " +
    "LEFT JOIN MemberEntity m2 ON m1.memberNumber + 1 = m2.memberNumber " +
    "WHERE m2.memberNumber is NULL")
    Optional<Integer> findLastUsedMemberNumberBeforeGap();

    /**
     * Queries for the lowest member number in the database.
     *
     * @return An optional Integer. Returns empty optional if there are no records in the table.
     */
    @Query("SELECT MIN(m1.memberNumber) from MemberEntity m1")
    Optional<Integer> findMinMemberNumber();

    /**
     * Queries for the highest member number in the database.
     *
     * @return An optional Integer.  Returns empty optional if there are no records in the table.
     */
    @Query("SELECT MAX(m1.memberNumber) from MemberEntity m1")
    Optional<Integer> findMaxMemberNumber();

    /**
     * Finds a member object by member number.
     * @param memberNumber The member number of the member object.
     * @return An {@link Optional} of type {@link MemberEntity}
     */
    Optional<MemberEntity> findByMemberNumber(int memberNumber);
}
