package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.RenewalsRepo;
import com.smh.club.api.hateoas.contracts.assemblers.RenewalAssembler;
import com.smh.club.api.hateoas.contracts.mappers.RenewalMapper;
import com.smh.club.api.hateoas.contracts.services.RenewalService;
import com.smh.club.api.hateoas.models.RenewalModel;
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
    public PagedModel<RenewalModel> getRenewalListPage(int pageNumber, int pageSize, String direction, String sort) {

        var pageRequest = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.fromString(direction),
            getSortColumn(sort));

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
    protected String getSortColumn(String key) {
        return getSort(key, RenewalModel.class, RenewalEntity.class).orElse("id");
    }
}
