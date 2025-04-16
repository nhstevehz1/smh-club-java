package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalMemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Component
public class RenewalMapperImpl extends DomainDataMapper implements RenewalMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public RenewalMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalEntity toEntity(RenewalDto dto) {
        return modelMapper.map(dto, RenewalEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalDto toDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalMemberDto toRenewalMemberDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalMemberDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalEntity updateEntity(RenewalDto dto, RenewalEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RenewalDto> toDtoList(List<RenewalEntity> entityList) {
        return mapList(entityList, RenewalDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<RenewalMemberDto> toPage(Page<RenewalEntity> page) {
        return page.map(this::toRenewalMemberDto);
    }
}
