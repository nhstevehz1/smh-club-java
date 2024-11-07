package com.smh.club.api.common.mappers;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.MemberCreateDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;

import java.util.List;

public interface MemberMapper {
    MemberEntity toMemberEntity(MemberCreateDto memberCreateDto);
    MemberDto toMemberDto(MemberEntity memberEntity);
    MemberEntity updateMemberEntity(MemberCreateDto memberDto, MemberEntity memberEntity);
    List<MemberDto> toMemberDtoList(List<MemberEntity> memberEntityList);
    MemberDetailDto toMemberDetailDto(MemberEntity entity);
}
