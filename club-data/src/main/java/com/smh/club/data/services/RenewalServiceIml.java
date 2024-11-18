package com.smh.club.data.services;

import com.smh.club.data.contracts.mappers.RenewalMapper;
import com.smh.club.data.contracts.services.RenewalService;
import com.smh.club.data.domain.entities.RenewalEntity;
import com.smh.club.data.domain.repos.MembersRepo;
import com.smh.club.data.domain.repos.RenewalsRepo;
import com.smh.club.data.dto.RenewalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class RenewalServiceIml extends AbstractServiceBase implements RenewalService {

    private final RenewalsRepo renewalRepo;
    private final MembersRepo memberRepo;
    private final RenewalMapper renewalMapper;

    @Override
    public Page<RenewalDto> getRenewalListPage(int pageNumber, int pageSize,
                                               @NonNull String direction, @NonNull String sort) {

        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.fromString(direction),
                getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return renewalRepo.findAll(pageRequest).map(renewalMapper::toDto);
    }

    @Override
    public Optional<RenewalDto> getRenewal(int id) {
        log.debug("Getting renewal by id: {}", id);

        return renewalRepo.findById(id).map(renewalMapper::toDto);
    }

    @Override
    public RenewalDto createRenewal(RenewalDto renewal) {
        log.debug("creating renewal: {}", renewal);

        var memberRef = memberRepo.getReferenceById(renewal.getMemberId());
        var addressEntity = renewalMapper.toEntity(renewal);
        addressEntity.setMember(memberRef);
        return renewalMapper.toDto(renewalRepo.save(addressEntity));
    }

    @Override
    public Optional<RenewalDto> updateRenewal(int id, RenewalDto renewalDto) {
        log.debug("Updating renewal, id: {}, with data: {}", id, renewalDto);

        return renewalRepo.findByIdAndMemberId(id, renewalDto.getMemberId())
                .map(r -> renewalMapper.updateEntity(renewalDto, r))
                .map(renewalMapper::toDto);
    }

    @Override
    public void deleteRenewal(int id) {
        log.debug("Deleting address, id: {}", id);
        renewalRepo.deleteById(id);
    }

    @Override
    public long getRenewalCount() {
        log.debug("Getting member count");
        return renewalRepo.count();
    }

    protected String getSortColumn(String key) {
        var source = RenewalDto.class;
        var target = RenewalEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
