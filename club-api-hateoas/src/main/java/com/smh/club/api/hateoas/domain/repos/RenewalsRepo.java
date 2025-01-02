package com.smh.club.api.hateoas.domain.repos;

import com.smh.club.api.hateoas.domain.entities.RenewalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


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
}
