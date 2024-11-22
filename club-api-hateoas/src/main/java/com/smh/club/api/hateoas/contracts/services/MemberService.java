package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.MemberModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface MemberService {
    PagedModel<MemberModel> getMemberListPage(int pageSize, int pageNumber, String direction, String sort);
    Optional<MemberModel> getMember(int id);
    MemberModel createMember(MemberModel member);
    Optional<MemberModel> updateMember(int id, MemberModel member);
    void deleteMember(int id);
    long getMemberCount();
}
