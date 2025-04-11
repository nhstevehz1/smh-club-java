package com.smh.club.api.domain.repos;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * A repository for {@link RenewalEntity} objects
 */
@Repository
public interface RenewalsRepo extends JpaRepository<RenewalEntity, Integer> {

    /**
     * Finds an address that matches an address id and a member id.
     * @param id The address object id.
     * @param memberId The member object id.
     * @return An {@link RenewalEntity}.
     */
    @Query(value = "SELECT * FROM member_mgmt.renewal r WHERE r.id = :id and r.member_id = :memberId", nativeQuery = true)
    Optional<RenewalEntity> findByIdAndMemberId(int id, int memberId);

    /**
     * Finds all renewals for a member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link RenewalEntity}'s.
     */
    List<RenewalEntity> findAllByMemberId(int memberId);
}
