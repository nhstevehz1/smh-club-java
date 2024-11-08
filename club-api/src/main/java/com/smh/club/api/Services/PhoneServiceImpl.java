package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;
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
    public PageResponse<PhoneDto> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting phone item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        
        log.debug("Created pageable: {}", pageRequest);

        var page = phoneRepo.findAll(pageRequest);

        return PageResponse.<PhoneDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(phoneMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<PhoneDto> getItem(int id) {
        log.debug("Getting phone by id: {}", id);

        return phoneRepo.findById(id).map(phoneMapper::toDto);
    }

    @Override
    public PhoneDto createItem(CreatePhoneDto createDto) {
        log.debug("creating phone: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var phoneEntity = phoneMapper.toEntity(createDto);
        phoneEntity.setMember(memberRef);
        return phoneMapper.toDto(phoneRepo.save(phoneEntity));
    }

    @Override
    public Optional<PhoneDto> updateItem(int id, UpdatePhoneDto updateDto) {
        log.debug("Updating phone, id: {}, with data: {}", id, updateDto);

        return phoneRepo.findByIdAndMemberId(id, updateDto.getMemberId())
                .map(e -> phoneMapper.updateEntity(updateDto, e))
                .map(phoneMapper::toDto);
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
        map.put("phone-number", "phoneNum");
        map.put("phone-type", "phoneType");
        return map;
    }
}
