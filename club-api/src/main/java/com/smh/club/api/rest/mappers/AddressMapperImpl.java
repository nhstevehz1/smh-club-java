package com.smh.club.api.rest.mappers;

import com.smh.club.api.rest.contracts.mappers.AddressMapper;
import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.dto.address.AddressCreateDto;
import com.smh.club.api.rest.dto.address.AddressDto;
import com.smh.club.api.rest.dto.address.AddressFullNameDto;
import com.smh.club.api.rest.dto.address.AddressUpdateDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 */
@Component
public class AddressMapperImpl extends DomainDataMapper implements AddressMapper  {

    /**
     * Constructor.
     * @param modelMapper A {@link ModelMapper} object.;
     */
    public AddressMapperImpl(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity toEntity(AddressCreateDto dto) {
        return modelMapper.map(dto, AddressEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressDto toDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressFullNameDto toAddressMemberDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressFullNameDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity updateEntity(AddressUpdateDto dto, AddressEntity entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AddressDto> toDtoList(List<AddressEntity> source) {
        return mapList(source, AddressDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AddressFullNameDto> toPage(Page<AddressEntity> page) {
        return page.map(this::toAddressMemberDto);
    }
}
