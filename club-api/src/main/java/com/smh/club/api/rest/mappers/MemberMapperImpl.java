package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.contracts.mappers.MemberMapper;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
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
    public MemberEntity toEntity(MemberDto dto)
    {
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
        return modelMapper.map(entity, MemberDetailDto.class);
    }
}
