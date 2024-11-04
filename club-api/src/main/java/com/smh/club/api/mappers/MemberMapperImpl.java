package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMapperImpl extends DataObjectMapperBase implements MemberMapper {
    public MemberMapperImpl(ModelMapper mapper) {
        super(mapper);
        configureMapper(mapper);
    }

    @Override
    public MemberEntity toEntity(MemberMinimumDto dataObject) {
        return modelMapper.map(dataObject, MemberEntity.class);
    }

    @Override
    public MemberDto toDto(MemberEntity entity) {
        return modelMapper.map(entity, MemberDto.class);
    }

    @Override
    public MemberEntity updateEntity(MemberMinimumDto dataObject, MemberEntity entity) {
        modelMapper.map(dataObject, entity);
        return entity;
    }

    @Override
    public List<MemberMinimumDto> toDtoList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberMinimumDto.class);
    }

    @Override
    public MemberDto toMemberDto(MemberEntity entity) {
        var dto = modelMapper.map(entity, MemberDto.class);
        dto.setAddresses(mapList(entity.getAddresses(), AddressDto.class));
        dto.setEmails(mapList(entity.getEmails(), EmailDto.class));
        dto.setPhones(mapList(entity.getPhones(), PhoneDto.class));
        dto.setRenewals(mapList(entity.getRenewals(), RenewalDto.class));
        return dto;
    }

    @Override
    protected void configureMapper(ModelMapper mapper) {

    }
}
