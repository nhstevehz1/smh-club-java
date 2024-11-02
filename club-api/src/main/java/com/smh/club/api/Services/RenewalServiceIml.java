package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
import com.smh.club.api.data.dto.RenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class RenewalServiceIml implements RenewalService {

    private final RenewalsRepo renewalRepo;
    private final MembersRepo memberRepo;

    private final RenewalMapper renewalMapper;

    private final Map<String, String> sortColumnMap = initSortColumnMap();
    
    @Override
    public PageResponse<RenewalDto> getItemListPage(PageParams pageParams) {
        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = renewalRepo.findAll(pageRequest);

        return PageResponse.<RenewalDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(renewalMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Optional<RenewalDto> getItem(int id) {
        log.debug("Getting renewal by id: {}", id);

        return renewalRepo.findById(id).map(renewalMapper::toDataObject);
    }

    @Override
    public RenewalDto createItem(RenewalDto renewal) {
        log.debug("creating renewal: {}", renewal);

        var memberRef = memberRepo.getReferenceById(renewal.getMemberId());
        var addressEntity = renewalMapper.toEntity(renewal);
        addressEntity.setMember(memberRef);
        return renewalMapper.toDataObject(renewalRepo.save(addressEntity));
    }

    @Override
    public Optional<RenewalDto> updateItem(int id, RenewalDto renewalDto) {
        log.debug("Updating renewal, id: {}, with data: {}", id, renewalDto);

        if(id != renewalDto.getId()) {
            throw new IllegalArgumentException();
        }

        return renewalRepo.findByIdAndMemberId(id, renewalDto.getMemberId())
                .map(r -> renewalMapper.updateEntity(renewalDto, r))
                .map(renewalMapper::toDataObject);
    }

    @Override
    public void deleteItem(int id) {
        log.debug("Deleting address, id: {}", id);
        renewalRepo.deleteById(id);
    }

    @Override
    public CountResponse getItemCount() {
        log.debug("Getting member count");
        return CountResponse.of(renewalRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "id");
        map.put("renewal-Date", "renewalDate");
        map.put("renewal-year", "renewalYear");

        return map;
    }
}
