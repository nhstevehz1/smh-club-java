package com.smh.club.api.hateoas.domain.repos;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberNumberOnly {
    int getMemberNumber();
}
