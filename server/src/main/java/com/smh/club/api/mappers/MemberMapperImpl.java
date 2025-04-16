package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.MemberMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.member.MemberDetailDto;
import com.smh.club.api.dto.member.MemberDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public MemberEntity toEntity(MemberDto dto) {
        return modelMapper.map(dto, MemberEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDto toDto(MemberEntity entity) {
        return modelMapper.map(entity, MemberDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity updateEntity(MemberDto createMemberDto, MemberEntity memberEntity) {
        modelMapper.map(createMemberDto, memberEntity);
        return memberEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDto> toDtoList(List<MemberEntity> entityList) {
        return mapList(entityList, MemberDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberDto> toPage(Page<MemberEntity> page) {
        return page.map(this::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDetailDto toMemberDetailDto(MemberEntity entity) {
        var details = modelMapper.map(entity, MemberDetailDto.class);
        entity.getAddresses().forEach(a -> details.getAddresses().add(modelMapper.map(a, AddressDto.class)));
        entity.getEmails().forEach(e -> details.getEmails().add(modelMapper.map(e, EmailDto.class)));
        entity.getPhones().forEach(p -> details.getPhones().add(modelMapper.map(p, PhoneDto.class)));
        entity.getRenewals().forEach(r -> details.getRenewals().add(modelMapper.map(r, RenewalDto.class)));
        return details;
    }
}
