package com.smh.club.oauth2.domain.repos;

import com.smh.club.oauth2.domain.entities.GrantedAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrantedAuthorityRepo extends JpaRepository<GrantedAuthorityEntity, Long> {
}
