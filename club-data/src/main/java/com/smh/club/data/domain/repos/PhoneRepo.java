package com.smh.club.data.domain.repos;

import com.smh.club.data.domain.entities.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepo extends JpaRepository<PhoneEntity, Integer> {
    @Query(value = "SELECT * FROM member_mgmt.phone p WHERE p.id = :id and p.member_id = :memberId", nativeQuery = true)
    Optional<PhoneEntity> findByIdAndMemberId(int id, int memberId);
}
