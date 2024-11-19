package com.smh.club.api.rest.contracts.mappers;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.dto.MemberDetailDto;
import com.smh.club.api.data.dto.MemberDto;

import java.util.List;

public interface MemberMapper {
    MemberEntity toEntity(MemberDto createMemberDto);
    MemberDto toDto(MemberEntity memberEntity);
    MemberEntity updateEntity(MemberDto memberDto, MemberEntity memberEntity);
    List<MemberDto> toDtoList(List<MemberEntity> memberEntityList);
    MemberDetailDto toMemberDetailDto(MemberEntity entity);
}
