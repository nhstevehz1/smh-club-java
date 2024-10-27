package com.smh.club.api.data.repos;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberNumberOnly {
    int getMemberNumber();
}
