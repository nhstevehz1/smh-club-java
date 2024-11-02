package com.smh.club.api.common.services;

import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.data.dto.MemberDetailDto;

import java.util.Optional;


public interface MemberService extends CrudService<MemberDto> {
    Optional<MemberDetailDto> getMemberDetail(int id);
}
