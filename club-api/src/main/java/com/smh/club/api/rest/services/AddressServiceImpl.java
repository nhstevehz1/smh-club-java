package com.smh.club.api.rest.services;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.rest.contracts.mappers.AddressMapper;
import com.smh.club.api.rest.contracts.services.AddressService;
import com.smh.club.api.rest.dto.AddressDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smh.club.api.shared.services.AbstractServiceBase;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements an {@link AddressService}.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
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
    public PagedDto<AddressDto> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = addressMapper.toPage(addressRepo.findAll(pageRequest));

        return PagedDto.of(page);
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
    protected Sort getSort(Sort sort) {
        if (sort.isUnsorted()) {
            return sort;
        }

        var orders =
            sort.get()
                .map(o -> new Sort.Order(o.getDirection(),
                    getSort(o.getProperty(), AddressDto.class, AddressEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
