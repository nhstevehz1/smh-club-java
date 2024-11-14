package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;

import java.util.List;

public interface MemberMapper {
    MemberEntity toMemberEntity(MemberDto createMemberDto);
    MemberDto toMemberDto(MemberEntity memberEntity);
    MemberEntity updateMemberEntity(MemberDto memberDto, MemberEntity memberEntity);
    List<MemberDto> toMemberDtoList(List<MemberEntity> memberEntityList);
    MemberDetailDto toMemberDetailDto(MemberEntity entity);
}
