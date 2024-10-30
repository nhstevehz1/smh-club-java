package com.smh.club.api.common.services;

import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;


public interface MemberService extends CrudService<Member> {
    MemberDetail getMemberDetail(int id);
}
