package com.smh.club.api.common.mappers;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.data.dto.MemberDetailDto;

public interface MemberMapper extends DataObjectMapper<MemberDto, MemberEntity> {
    MemberDetailDto toMemberDetail(MemberEntity entity);
}
