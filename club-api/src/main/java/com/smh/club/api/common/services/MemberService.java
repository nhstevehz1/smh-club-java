package com.smh.club.api.common.services;

import com.smh.club.api.dto.CreateMemberDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;


public interface MemberService {
    PageResponse<MemberDto> getMemberListPage(PageParams pageParams);
    Optional<MemberDto> getMember(int id);
    MemberDto createMember(CreateMemberDto member);
    Optional<MemberDto> updateMember(int id, CreateMemberDto member);
    void deleteMember(int id);
    CountResponse getMemberCount();
    Optional<MemberDetailDto> getMemberDetail(int id);
}
