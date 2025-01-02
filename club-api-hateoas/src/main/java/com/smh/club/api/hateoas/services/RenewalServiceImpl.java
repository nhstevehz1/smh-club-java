package com.smh.club.api.hateoas.services;

import com.smh.club.api.hateoas.contracts.assemblers.RenewalAssembler;
import com.smh.club.api.hateoas.contracts.mappers.RenewalMapper;
import com.smh.club.api.hateoas.contracts.services.RenewalService;
import com.smh.club.api.hateoas.domain.entities.RenewalEntity;
import com.smh.club.api.hateoas.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.domain.repos.RenewalsRepo;
import com.smh.club.api.hateoas.models.RenewalModel;
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
 * Extends an {@link AbstractServiceBase} and implements a {@link RenewalService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RenewalServiceImpl extends AbstractServiceBase implements RenewalService {

    private final RenewalsRepo renewalRepo;

    private final MembersRepo membersRepo;

    private final RenewalAssembler assembler;

    private final RenewalMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedModel<RenewalModel> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        return assembler.toPagedModel(renewalRepo.findAll(pageRequest));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RenewalModel> getRenewal(int id) {
        log.debug("Getting renewal by id: {}", id);
        return renewalRepo.findById(id).map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalModel createRenewal(RenewalModel renewal) {
        log.debug("Creating renewal: {}", renewal);

        var member = membersRepo.getReferenceById(renewal.getMemberId());
        var entity = mapper.toEntity(renewal);
        entity.setMember(member);

        return assembler.toModel(renewalRepo.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RenewalModel> updateRenewal(int id, RenewalModel renewal) {
        log.debug("Updating renewal id: {}, with data: {}", id, renewal);

        return renewalRepo.findByIdAndMemberId(id, renewal.getMemberId())
            .map(entity -> mapper.updateEntity(renewal, entity))
            .map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRenewal(int id) {
        log.debug("Deleting renewal id: {}", id);
        renewalRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRenewalCount() {
        return renewalRepo.count();
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
                    getSort(o.getProperty(), RenewalModel.class, RenewalEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
