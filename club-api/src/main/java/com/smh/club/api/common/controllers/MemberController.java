package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.MemberMinimumDto;
import com.smh.club.api.dto.MemberDto;
import org.springframework.http.ResponseEntity;

public interface MemberController extends CrudController<MemberMinimumDto>{
    ResponseEntity<MemberDto> getMemberDetail(int id);
}
