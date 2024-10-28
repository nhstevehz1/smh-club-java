package com.smh.club.api.Services;

import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface MemberService {

    PageResponse<Member> getMemberPage(Optional<PageParams> pageRequest);

    MemberDetail getMemberDetail(int id);

    Member createMember(Member member);

    Member updateMember(Member member);

    void deleteMember(int id);

    CountResponse getMemberCount();
}
