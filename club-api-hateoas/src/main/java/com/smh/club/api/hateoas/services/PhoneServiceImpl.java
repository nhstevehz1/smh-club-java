package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.PhoneRepo;
import com.smh.club.api.hateoas.contracts.assemblers.PhoneAssembler;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.contracts.services.PhoneService;
import com.smh.club.api.hateoas.models.PhoneModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smh.club.shared.services.AbstractServiceBase;

import java.util.Optional;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements a {@link PhoneService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PhoneServiceImpl extends AbstractServiceBase implements PhoneService {

    private final PhoneRepo phoneRepo;

    private final MembersRepo membersRepo;

    private final PhoneAssembler assembler;

    private final PhoneMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<PhoneModel> getPhoneListPage(int pageNumber, int pageSize, String direction, String sort) {

        var pageRequest = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.fromString(direction),
            getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return assembler.toPagedModel(phoneRepo.findAll(pageRequest));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PhoneModel> getPhone(int id) {
        log.debug("Getting phone by id: {}", id);
        return phoneRepo.findById(id).map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneModel createPhone(PhoneModel phone) {
        log.debug("Creating phone: {}", phone);

        var member = membersRepo.getReferenceById(phone.getMemberId());
        var entity = mapper.toEntity(phone);
        entity.setMember(member);

        return assembler.toModel(phoneRepo.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PhoneModel> updatePhone(int id, PhoneModel phone) {
        log.debug("Updating phone id: {}, with data: {}", id, phone);

        return phoneRepo.findByIdAndMemberId(id, phone.getMemberId())
            .map(entity -> mapper.updateEntity(phone, entity))
            .map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePhone(int id) {
        log.debug("Deleting phone id: {}", id);
        phoneRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPhoneCount() {
        return phoneRepo.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSortColumn(String key) {
        var source = PhoneModel.class;
        var target = PhoneEntity.class;

        return getSort(key, source, target).orElse("id");
    }
}
