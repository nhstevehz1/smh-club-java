package com.smh.club.api.mappers;

import com.smh.club.api.contracts.mappers.AddressMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressMemberDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public AddressEntity toEntity(AddressDto dto) {
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
    public AddressMemberDto toAddressMemberDto(AddressEntity entity) {
        return modelMapper.map(entity, AddressMemberDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressEntity updateEntity(AddressDto dto, AddressEntity entity) {
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
    public Page<AddressMemberDto> toPage(Page<AddressEntity> page) {
        return page.map(this::toAddressMemberDto);
    }
}
