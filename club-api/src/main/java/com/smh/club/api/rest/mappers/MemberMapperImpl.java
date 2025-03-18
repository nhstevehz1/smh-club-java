package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.contracts.mappers.MemberMapper;
import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.rest.dto.member.MemberCreateDto;
import com.smh.club.api.rest.dto.member.MemberDetailDto;
import com.smh.club.api.rest.dto.member.MemberMinDto;
import com.smh.club.api.rest.dto.member.MemberUpdateDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class MemberMapperImpl extends DomainDataMapper implements MemberMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public MemberMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity toEntity(MemberCreateDto dto) {
        var entity = modelMapper.map(dto, MemberEntity.class);
        dto.getAddresses().forEach(a -> entity.addAddress(modelMapper.map(a, AddressEntity.class)));
        dto.getEmails().forEach(e -> entity.addEmail(modelMapper.map(e, EmailEntity.class)));
        dto.getPhones().forEach(p -> entity.addPhone(modelMapper.map(p, PhoneEntity.class)));
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberMinDto toDto(MemberEntity entity) {
        return modelMapper.map(entity, MemberMinDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity updateEntity(MemberUpdateDto createMemberDto, MemberEntity memberEntity) {
        modelMapper.map(createMemberDto, memberEntity);
        return memberEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberMinDto> toDtoList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberMinDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberMinDto> toPage(Page<MemberEntity> page) {
        return page.map(this::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDetailDto toMemberDetailDto(MemberEntity entity) {
        return modelMapper.map(entity, MemberDetailDto.class);
    }
}
