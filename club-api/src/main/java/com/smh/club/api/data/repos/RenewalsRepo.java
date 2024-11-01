package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.RenewalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RenewalsRepo extends JpaRepository<RenewalEntity, Integer> {
    @Query(value = "SELECT * FROM member_mgmt.renewals r WHERE r.id = :id and r.member_id = :memberId", nativeQuery = true)
    Optional<RenewalEntity> findByIdAndMemberId(int id, int memberId);
}
