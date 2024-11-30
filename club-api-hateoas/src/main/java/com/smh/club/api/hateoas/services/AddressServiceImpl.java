package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.data.repos.AddressRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.hateoas.contracts.assemblers.AddressAssembler;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.contracts.services.AddressService;
import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.shared.services.AbstractServiceBase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final MembersRepo membersRepo;

    private final AddressAssembler assembler;

    private final AddressMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<AddressModel> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        return assembler.toPagedModel(addressRepo.findAll(pageRequest));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AddressModel> getAddress(int id) {
        log.debug("Getting address by id: {}", id);
        return addressRepo.findById(id).map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddressModel createAddress(AddressModel address) {
        log.debug("Creating address: {}", address);

        var member = membersRepo.getReferenceById(address.getMemberId());
        var entity = mapper.toEntity(address);
        entity.setMember(member);

        return assembler.toModel(addressRepo.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AddressModel> updateAddress(int id, AddressModel address) {
        log.debug("Updating address id: {}, with data: {}", id, address);

        return addressRepo.findByIdAndMemberId(id, address.getMemberId())
            .map(entity -> mapper.updateEntity(address, entity))
            .map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAddress(int id) {
        log.debug("Deleting address id: {}", id);
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
                    getSort(o.getProperty(), AddressModel.class, AddressEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
