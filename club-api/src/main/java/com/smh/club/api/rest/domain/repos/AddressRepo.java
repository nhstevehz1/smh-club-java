package com.smh.club.api.rest.domain.repos;

import com.smh.club.api.rest.domain.entities.AddressEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A repository for {@link AddressEntity} objects
 */
@Repository
public interface AddressRepo extends JpaRepository<AddressEntity, Integer> {

    /**
     * Finds an address that matches an address id and a member id.
     * @param id The address object id.
     * @param memberId The member object id.
     * @return An {@link AddressEntity}.
     */
    @Query(value = "SELECT * FROM member_mgmt.address a WHERE a.id = :id and a.member_id = :memberId", nativeQuery = true)
    Optional<AddressEntity> findByIdAndMemberId( int id, int memberId);

}
