package com.smh.club.api.services;

import com.smh.club.api.contracts.mappers.RenewalMapper;
import com.smh.club.api.contracts.services.RenewalService;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalMemberDto;
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
 * Extends an {@link AbstractServiceBase} and implements an {@link RenewalService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RenewalServiceIml extends AbstractServiceBase implements RenewalService {

    private final RenewalsRepo renewalRepo;
    private final MembersRepo memberRepo;
    private final RenewalMapper renewalMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedDto<RenewalMemberDto> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSortString(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = renewalMapper.toPage(renewalRepo.findAll(pageRequest));

        return PagedDto.of(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RenewalDto> findAllByMemberId(int id) {
        var renewals = renewalRepo.findAllByMemberId(id);
        return renewalMapper.toDtoList(renewals);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RenewalDto> getRenewal(int id) {
        log.debug("Getting renewal by id: {}", id);

        return renewalRepo.findById(id).map(renewalMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenewalDto createRenewal(RenewalDto renewal) {
        log.debug("creating renewal: {}", renewal);

        var memberRef = memberRepo.getReferenceById(renewal.getMemberId());
        var addressEntity = renewalMapper.toEntity(renewal);
        addressEntity.setMember(memberRef);
        return renewalMapper.toDto(renewalRepo.save(addressEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RenewalDto> updateRenewal(int id, RenewalDto renewalDto) {
        log.debug("Updating renewal, id: {}, with data: {}", id, renewalDto);

        return renewalRepo.findByIdAndMemberId(id, renewalDto.getMemberId())
                .map(r -> renewalMapper.updateEntity(renewalDto, r))
                .map(renewalMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRenewal(int id) {
        log.debug("Deleting address, id: {}", id);
        renewalRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRenewalCount() {
        log.debug("Getting member count");
        return renewalRepo.count();
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
                    getSortString(o.getProperty(), RenewalMemberDto.class, RenewalEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
