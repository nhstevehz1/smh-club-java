package com.smh.club.api.mappers;

import com.smh.club.api.rest.contracts.mappers.PhoneMapper;
import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneFullNameDto;
import com.smh.club.api.dto.phone.PhoneUpdateDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
    public PhoneEntity toEntity(PhoneCreateDto createPhoneDto) {
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
    public PhoneFullNameDto toPhoneMemberDto(PhoneEntity entity) {
        return modelMapper.map(entity, PhoneFullNameDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneEntity updateEntity(PhoneUpdateDto dto, PhoneEntity entity) {
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
    public Page<PhoneFullNameDto> toPage(Page<PhoneEntity> page) {
        return page.map(this::toPhoneMemberDto);
    }
}
