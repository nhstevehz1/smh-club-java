package com.smh.club.api.common.controllers;

import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
import org.springframework.http.ResponseEntity;

public interface MemberController extends CrudController<Member>{
    ResponseEntity<MemberDetail> getMemberDetail(int id);
}
