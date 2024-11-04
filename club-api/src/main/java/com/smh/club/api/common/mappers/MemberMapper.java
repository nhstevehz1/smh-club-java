package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.dto.MemberMinimumDto;

public interface MemberMapper extends DataObjectMapper<MemberMinimumDto, MemberEntity> {
    MemberDto toMemberDto(MemberEntity entity);
}
