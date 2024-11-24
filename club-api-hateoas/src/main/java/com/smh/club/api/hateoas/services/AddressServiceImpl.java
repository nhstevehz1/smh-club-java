package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.contracts.assemblers.AddressAssembler;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.contracts.services.AddressService;
import com.smh.club.api.hateoas.models.AddressModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final MembersRepo membersRepo;

    private final AddressAssembler assembler;

    private final AddressMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<AddressModel> getAddressListPage(int pageNumber, int pageSize, String direction, String sort) {

        var pageRequest = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.fromString(direction),
            getSortColumn(sort));

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
    protected String getSortColumn(String key) {
        return getSort(key, AddressModel.class, AddressEntity.class).orElse("id");
    }
}
