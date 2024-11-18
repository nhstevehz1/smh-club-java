package com.smh.club.api.rest.contracts;

import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.data.dto.MemberDetailDto;
import com.smh.club.api.data.dto.MemberDto;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<PageResponse<MemberDto>> page(int pageNumber, int pageSize, String sortDir, String sort);
    ResponseEntity<MemberDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<MemberDto> create(MemberDto member);
    ResponseEntity<MemberDto> update(int id, MemberDto member);
    ResponseEntity<Void> delete(int id);
    ResponseEntity<MemberDetailDto> detail(int id);
}
