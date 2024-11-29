package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.PhoneRepo;
import com.smh.club.api.hateoas.contracts.assemblers.PhoneAssembler;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.contracts.services.PhoneService;
import com.smh.club.api.hateoas.models.PhoneModel;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smh.club.shared.api.services.AbstractServiceBase;

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
    public PagedModel<PhoneModel> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = phoneRepo.findAll(pageRequest);

        return assembler.toPagedModel(page);
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
    protected Sort getSort(Sort sort) {
        if (sort.isUnsorted()) {
            return sort;
        }

        var orders =
            sort.get()
                .map(o -> new Sort.Order(o.getDirection(),
                    getSort(o.getProperty(), PhoneModel.class, PhoneEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
