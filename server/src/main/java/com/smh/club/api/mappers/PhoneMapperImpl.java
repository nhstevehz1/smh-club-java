package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneMemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Component
public class PhoneMapperImpl extends DomainDataMapper implements PhoneMapper {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public PhoneMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity toEntity(PhoneDto createPhoneDto) {
        return modelMapper.map(createPhoneDto, PhoneEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneDto toDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneMemberDto toPhoneMemberDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneMemberDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity updateEntity(PhoneDto dto, PhoneEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PhoneDto> toDtoList(List<PhoneEntity> entityList) {
        return mapList(entityList, PhoneDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PhoneMemberDto> toPage(Page<PhoneEntity> page) {
        return page.map(this::toPhoneMemberDto);
    }
}
