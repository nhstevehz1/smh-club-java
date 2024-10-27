package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembersRepo extends JpaRepository<MemberEntity, Integer> {
    boolean existsByMemberNumber(int memberNumber);

    List<MemberNumberOnly> findByMemberNumberGreaterThanEqualAndMemberNumberLessThanEqual(int low, int high);

    Optional<MemberEntity> findByMemberNumber(int memberNumber);
}
