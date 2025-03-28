package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.renewal.RenewalCreateDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalFullNameDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
    public RenewalEntity toEntity(RenewalCreateDto dto) {
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
    public RenewalFullNameDto toRenewalMemberDto(RenewalEntity entity) {
        return modelMapper.map(entity, RenewalFullNameDto.class);
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
    public Page<RenewalFullNameDto> toPage(Page<RenewalEntity> page) {
        return page.map(this::toRenewalMemberDto);
    }
}
