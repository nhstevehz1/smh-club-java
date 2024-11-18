package com.smh.club.api.data.domain.repos;

import com.smh.club.api.data.domain.entities.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepo extends JpaRepository<EmailEntity, Integer> {

    @Query(value = "SELECT * FROM member_mgmt.email a WHERE a.id = :id and a.member_id = :memberId", nativeQuery = true)
    Optional<EmailEntity> findByIdAndMemberId(int id, int memberId);
}
