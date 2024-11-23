package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.contracts.mappers.MemberMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMapperImpl extends DomainDataMapper implements MemberMapper {

    public MemberMapperImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public MemberEntity toEntity(MemberDto createMemberDto)
    {
        return modelMapper.map(createMemberDto, MemberEntity.class);
    }

    @Override
    public MemberDto toDto(MemberEntity memberEntity) {
        return modelMapper.map(memberEntity, MemberDto.class);
    }

    @Override
    public MemberEntity updateEntity(MemberDto createMemberDto, MemberEntity memberEntity) {
        modelMapper.map(createMemberDto, memberEntity);
        return memberEntity;
    }

    @Override
    public List<MemberDto> toDtoList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberDto.class);
    }

    @Override
    public MemberDetailDto toMemberDetailDto(MemberEntity entity) {
        return modelMapper.map(entity, MemberDetailDto.class);
    }
}
