package com.smh.club.api.common.controllers;

import com.smh.club.data.dto.MemberDetailDto;
import com.smh.club.data.dto.MemberDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<PageResponse<MemberDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<MemberDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<MemberDto> create(MemberDto member);
    ResponseEntity<MemberDto> update(int id, MemberDto member);
    ResponseEntity<Void> delete(int id);
    ResponseEntity<MemberDetailDto> detail(int id);
}
