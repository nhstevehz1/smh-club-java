package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.RenewalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenewalsRepo extends JpaRepository<RenewalEntity, Integer> {
}
