package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMapperImpl extends DomainDataMapper implements MemberMapper {

    public MemberMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public MemberEntity toMemberEntity(MemberDto createMemberDto)
    {
        return modelMapper.map(createMemberDto, MemberEntity.class);
    }

    @Override
    public MemberDto toMemberDto(MemberEntity memberEntity) {
        return modelMapper.map(memberEntity, MemberDto.class);
    }

    @Override
    public MemberEntity updateMemberEntity(MemberDto createMemberDto, MemberEntity memberEntity) {
        modelMapper.map(createMemberDto, memberEntity);
        return memberEntity;
    }

    @Override
    public List<MemberDto> toMemberDtoList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberDto.class);
    }

    @Override
    public MemberDetailDto toMemberDetailDto(MemberEntity entity) {
        var dto = modelMapper.map(entity, MemberDetailDto.class);
        dto.setAddresses(mapList(entity.getAddresses(), AddressDto.class));
        dto.setEmails(mapList(entity.getEmails(), EmailDto.class));
        dto.setPhones(mapList(entity.getPhones(), PhoneDto.class));
        dto.setRenewals(mapList(entity.getRenewals(), RenewalDto.class));
        return dto;
    }
}
