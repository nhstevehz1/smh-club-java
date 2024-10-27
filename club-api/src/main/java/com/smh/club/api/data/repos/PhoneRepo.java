package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepo extends JpaRepository<PhoneEntity, Integer> {
}
