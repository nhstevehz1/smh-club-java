package com.smh.club.data.domain.repos;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberNumberOnly {
    int getMemberNumber();
}
