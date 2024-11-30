package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.EmailEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepo extends JpaRepository<EmailEntity, Integer> {

    /**
     * Finds an address that matches an address id and a member id.
     * @param id The address object id.
     * @param memberId The member object id.
     * @return An {@link EmailEntity}.
     */
    @Query(value = "SELECT * FROM member_mgmt.email a WHERE a.id = :id and a.member_id = :memberId", nativeQuery = true)
    Optional<EmailEntity> findByIdAndMemberId(int id, int memberId);
}
