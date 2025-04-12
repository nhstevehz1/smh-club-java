package com.smh.club.api.domain.repos;

import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


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

    /**
     * Finds all emails for a member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link EmailEntity}'s.
     */
    List<EmailEntity> findAllByMemberId(int memberId);
}
