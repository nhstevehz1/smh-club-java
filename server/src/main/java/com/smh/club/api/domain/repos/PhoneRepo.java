package com.smh.club.api.domain.repos;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A repository for {@link PhoneEntity} objects
 */
@Repository
public interface PhoneRepo extends JpaRepository<PhoneEntity, Integer> {

    /**
     * Finds an address that matches an address id and a member id.
     * @param id The address object id.
     * @param memberId The member object id.
     * @return An {@link PhoneEntity}.
     */@Query(value = "SELECT * FROM member_mgmt.phone p WHERE p.id = :id and p.member_id = :memberId", nativeQuery = true)
    Optional<PhoneEntity> findByIdAndMemberId(int id, int memberId);

    /**
     * Finds all phones for a member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link PhoneEntity}'s.
     */
     List<PhoneEntity> findAllByMemberId(int memberId);
}
