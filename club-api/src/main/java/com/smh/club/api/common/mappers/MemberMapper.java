package com.smh.club.api.common.mappers;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;

public interface MemberMapper extends DataObjectMapper<Member, MemberEntity> {
    MemberDetail toMemberDetail(MemberEntity entity);
}
