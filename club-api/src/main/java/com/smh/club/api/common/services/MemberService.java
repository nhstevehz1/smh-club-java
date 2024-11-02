package com.smh.club.api.common.services;

import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;

import java.util.Optional;


public interface MemberService extends CrudService<Member> {
    Optional<MemberDetail> getMemberDetail(int id);
}
