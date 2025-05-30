package com.smh.club.api.services;

import com.smh.club.api.contracts.mappers.AddressMapper;
import com.smh.club.api.contracts.services.AddressService;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressMemberDto;
import com.smh.club.api.response.PagedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements an {@link AddressService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AddressServiceImpl extends AbstractServiceBase implements AddressService {

    private final AddressRepo addressRepo;
    private final MembersRepo memberRepo;
    private final AddressMapper addressMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedDto<AddressMemberDto> getPage(Pageable pageable) {
        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSortString(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = addressMapper.toPage(addressRepo.findAll(pageRequest));

        return PagedDto.of(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AddressDto> findAllByMemberId(int id) {
        var addresses = addressRepo.findAllByMemberId(id);
        return addressMapper.toDtoList(addresses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AddressDto> getAddress(int id) {
        log.debug("Getting address by id: {}", id);

        return addressRepo.findById(id).map(addressMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressDto createAddress(AddressDto address) {
        log.debug("creating address: {}", address);

        var memberRef = memberRepo.getReferenceById(address.getMemberId());

        var addressEntity = addressMapper.toEntity(address);
        addressEntity.setMember(memberRef);
        return addressMapper.toDto(addressRepo.save(addressEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AddressDto> updateAddress(int id, AddressDto addressDto) {
        log.debug("Updating address id: {}, with data: {}", id, addressDto);

        return addressRepo.findByIdAndMemberId(id, addressDto.getMemberId())
                .map(e -> addressMapper.updateEntity(addressDto, e))
                .map(addressMapper::toDto);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAddress(int id) {
        log.debug("Deleting address, id: {}", id);
        addressRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAddressCount() {
        return addressRepo.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Sort getSortString(Sort sort) {
        if (sort.isUnsorted()) {
            return sort;
        }

        var orders =
            sort.get()
                .map(o -> new Sort.Order(o.getDirection(),
                    getSortString(o.getProperty(), AddressMemberDto.class, AddressEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
