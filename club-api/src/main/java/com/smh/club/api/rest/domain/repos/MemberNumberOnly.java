package com.smh.club.api.rest.domain.repos;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberNumberOnly {
    int getMemberNumber();
}
