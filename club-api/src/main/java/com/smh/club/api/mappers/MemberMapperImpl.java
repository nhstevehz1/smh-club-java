package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.data.dto.MemberDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberMapperImpl implements MemberMapper {
    public MemberEntity toEntity(MemberDto dataObject) {
        return MemberEntity.builder()
                .memberNumber(dataObject.getMemberNumber())
                .firstName(dataObject.getFirstName())
                .middleName(dataObject.getMiddleName())
                .lastName(dataObject.getLastName())
                .suffix(dataObject.getSuffix())
                .birthDate(dataObject.getBirthDate())
                .joinedDate(dataObject.getJoinedDate())
                .build();
    }

    @Override
    public MemberDto toDataObject(MemberEntity entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .memberNumber(entity.getMemberNumber())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .suffix(entity.getSuffix())
                .birthDate(entity.getBirthDate())
                .joinedDate(entity.getJoinedDate())
                .build();
    }

    @Override
    public List<MemberDto> toDataObjectList(List<MemberEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }

    @Override
    public MemberEntity updateEntity(MemberDto dataObject, MemberEntity entity) {
        entity.setMemberNumber(dataObject.getMemberNumber());
        entity.setFirstName(dataObject.getFirstName());
        entity.setMiddleName(dataObject.getMiddleName());
        entity.setLastName(dataObject.getLastName());
        entity.setSuffix(dataObject.getSuffix());
        entity.setBirthDate(dataObject.getBirthDate());
        entity.setJoinedDate(dataObject.getJoinedDate());
        return entity;
    }

    @Override
    public MemberDetailDto toMemberDetail(MemberEntity entity) {
        return MemberDetailDto.builder()
                .id(entity.getId())
                .memberNumber(entity.getMemberNumber())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .suffix(entity.getSuffix())
                .birthDate(entity.getBirthDate())
                .joinedDate(entity.getJoinedDate())
                .build();
    }
}
