package com.smh.club.api.common.controllers;

import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.data.dto.MemberDetailDto;
import org.springframework.http.ResponseEntity;

public interface MemberController extends CrudController<MemberDto>{
    ResponseEntity<MemberDetailDto> getMemberDetail(int id);
}
