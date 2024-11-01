package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.PhoneRepo;
import com.smh.club.api.models.Phone;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.NonNull;
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
public class PhoneServiceImpl implements PhoneService {
    
    private final PhoneRepo phoneRepo;
    private final MembersRepo memberRepo;
    
    private final PhoneMapper phoneMapper;

    private final Map<String, String> sortColumnMap = initSortColumnMap();
    
    @Override
    public PageResponse<Phone> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting phone item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        
        log.debug("Created pageable: {}", pageRequest);

        var page = phoneRepo.findAll(pageRequest);

        return PageResponse.<Phone>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(phoneMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Optional<Phone> getItem(int id) {
        log.debug("Getting phone by id: {}", id);

        return phoneRepo.findById(id).map(phoneMapper::toDataObject);
    }

    @Override
    public Phone createItem(Phone phone) {
        log.debug("creating phone: {}", phone);

        var memberRef = memberRepo.getReferenceById(phone.getMemberId());
        var phoneEntity = phoneMapper.toEntity(phone);
        phoneEntity.setMember(memberRef);
        return phoneMapper.toDataObject(phoneRepo.save(phoneEntity));
    }

    @Override
    public Optional<Phone> updateItem(int id, Phone phone) {
        log.debug("Updating phone, id: {}, with data: {}", id, phone);

        if(id != phone.getId()) {
            throw new IllegalArgumentException();
        }

        return phoneRepo.findByIdAndMemberId(id, phone.getMemberId())
                .map(e -> phoneMapper.updateEntity(phone, e))
                .map(phoneMapper::toDataObject);
    }

    @Override
    public void deleteItem(int id) {
        log.debug("Deleting address, id: {}", id);
        phoneRepo.deleteById(id);
    }

    @Override
    public CountResponse getItemCount() {
        log.debug("Getting member count");
        return CountResponse.of(phoneRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "id");
        map.put("phone-num", "phoneNum");
        map.put("phone-type", "phoneType");

        return map;
    }
}
