package com.smh.club.api.services;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.CreateRenewalDto;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public PageResponse<RenewalDto> getRenewalListPage(PageParams pageParams) {

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                getSortColumn(pageParams.getSortColumn()));

        log.debug("Created pageable: {}", pageRequest);

        var page = renewalRepo.findAll(pageRequest);

        return PageResponse.<RenewalDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(renewalMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<RenewalDto> getRenewal(int id) {
        log.debug("Getting renewal by id: {}", id);

        return renewalRepo.findById(id).map(renewalMapper::toDto);
    }

    @Override
    public RenewalDto createRenewal(CreateRenewalDto renewal) {
        log.debug("creating renewal: {}", renewal);

        var memberRef = memberRepo.getReferenceById(renewal.getMemberId());
        var addressEntity = renewalMapper.toEntity(renewal);
        addressEntity.setMember(memberRef);
        return renewalMapper.toDto(renewalRepo.save(addressEntity));
    }

    @Override
    public Optional<RenewalDto> updateRenewal(int id, CreateRenewalDto renewalDto) {
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
    public CountResponse getRenewalCount() {
        log.debug("Getting member count");
        return CountResponse.of(renewalRepo.count());
    }

    protected String getSortColumn(String key) {
        var source = RenewalDto.class;
        var target = RenewalEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
