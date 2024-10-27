package com.smh.club.api.data.repos;

import com.smh.club.api.data.entities.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepo extends JpaRepository<EmailEntity, Integer> {
}
