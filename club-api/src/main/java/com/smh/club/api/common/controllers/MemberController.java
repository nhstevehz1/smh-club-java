package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.CreateMemberDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<PageResponse<MemberDto>> getMemberListPage(int page, int size, String sortDir, String sort);
    ResponseEntity<MemberDto> getMember(int id);
    ResponseEntity<CountResponse> getCount();
    ResponseEntity<MemberDto> createMember(CreateMemberDto member);
    ResponseEntity<MemberDto> updateMember(int id, CreateMemberDto member);
    ResponseEntity<Void> deleteMember(int id);
    ResponseEntity<MemberDetailDto> getMemberDetail(int id);
}
