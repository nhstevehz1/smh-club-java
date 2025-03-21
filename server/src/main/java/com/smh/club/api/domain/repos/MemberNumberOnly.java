package com.smh.club.api.domain.repos;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberNumberOnly {
    int getMemberNumber();
}
