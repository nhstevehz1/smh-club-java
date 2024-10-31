package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
import com.smh.club.api.models.Renewal;
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
    public PageResponse<Renewal> getItemListPage(PageParams pageParams) {
        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = renewalRepo.findAll(pageRequest);

        return PageResponse.<Renewal>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(renewalMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Renewal getItem(int id) {
        log.debug("Getting renewal by id: {}", id);

        var renewalEntity = renewalRepo.findById(id).orElseThrow();
        return renewalMapper.toDataObject(renewalEntity);
    }

    @Override
    public Renewal createItem(Renewal renewal) {
        log.debug("creating renewal: {}", renewal);

        var memberEntity = memberRepo.findById(renewal.getMemberId()).orElseThrow();
        var addressEntity = renewalMapper.toEntity(renewal);
        addressEntity.setMember(memberEntity);
        return renewalMapper.toDataObject(renewalRepo.save(addressEntity));
    }

    @Override
    public Renewal updateItem(int id, Renewal renewal) {
        log.debug("Updating renewal, id: {}, with data: {}", id, renewal);

        if(id != renewal.getId()) {
            throw new IllegalArgumentException();
        }

        var renewalEntity = renewalRepo.findById(id).orElseThrow();

        renewalMapper.updateEntity(renewal, renewalEntity);
        renewalRepo.save(renewalEntity);

        return renewalMapper.toDataObject(renewalEntity);
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
