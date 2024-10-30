package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberMapperImpl implements MemberMapper {
    public MemberEntity toEntity(Member dataObject) {
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
    public Member toDataObject(MemberEntity entity) {
        return Member.builder()
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
    public List<Member> toDataObjectList(List<MemberEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }

    @Override
    public void update(Member dataObject, MemberEntity entity) {
        entity.setMemberNumber(dataObject.getMemberNumber());
        entity.setFirstName(dataObject.getFirstName());
        entity.setMiddleName(dataObject.getMiddleName());
        entity.setLastName(dataObject.getLastName());
        entity.setSuffix(dataObject.getSuffix());
        entity.setBirthDate(dataObject.getBirthDate());
        entity.setJoinedDate(dataObject.getJoinedDate());
    }

    @Override
    public MemberDetail toMemberDetail(MemberEntity entity) {
        return MemberDetail.builder()
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
