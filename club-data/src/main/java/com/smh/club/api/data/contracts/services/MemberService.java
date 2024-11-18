package com.smh.club.api.data.contracts.services;

import com.smh.club.api.data.dto.MemberDetailDto;
import com.smh.club.api.data.dto.MemberDto;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface MemberService {
    Page<MemberDto> getMemberListPage(int pageSize, int pageNumber, String direction, String sort);
    Optional<MemberDto> getMember(int id);
    MemberDto createMember(MemberDto member);
    Optional<MemberDto> updateMember(int id, MemberDto member);
    void deleteMember(int id);
    long getMemberCount();
    Optional<MemberDetailDto> getMemberDetail(int id);
}
