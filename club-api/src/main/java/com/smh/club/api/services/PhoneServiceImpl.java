package com.smh.club.api.services;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class PhoneServiceImpl extends AbstractServiceBase implements PhoneService {
    
    private final PhoneRepo phoneRepo;
    private final MembersRepo memberRepo;
    private final PhoneMapper phoneMapper;

    @Override
    public PageResponse<PhoneDto> getPhoneListPage(@NonNull PageParams pageParams) {
        log.debug("Getting phone item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                getSortColumn(pageParams.getSortColumn()));
        
        log.debug("Created pageable: {}", pageRequest);

        var page = phoneRepo.findAll(pageRequest);

        return PageResponse.<PhoneDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(phoneMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<PhoneDto> getPhone(int id) {
        log.debug("Getting phone by id: {}", id);

        return phoneRepo.findById(id).map(phoneMapper::toDto);
    }

    @Override
    public PhoneDto createPhone(CreatePhoneDto createDto) {
        log.debug("creating phone: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var phoneEntity = phoneMapper.toEntity(createDto);
        phoneEntity.setMember(memberRef);
        return phoneMapper.toDto(phoneRepo.save(phoneEntity));
    }

    @Override
    public Optional<PhoneDto> updatePhone(int id, CreatePhoneDto updateDto) {
        log.debug("Updating phone, id: {}, with data: {}", id, updateDto);

        return phoneRepo.findByIdAndMemberId(id, updateDto.getMemberId())
                .map(e -> phoneMapper.updateEntity(updateDto, e))
                .map(phoneMapper::toDto);
    }

    @Override
    public void deletePhone(int id) {
        log.debug("Deleting address, id: {}", id);
        phoneRepo.deleteById(id);
    }

    @Override
    public CountResponse getPhoneCount() {
        log.debug("Getting member count");
        return CountResponse.of(phoneRepo.count());
    }

    protected String getSortColumn(String key) {
        var source = PhoneDto.class;
        var target = PhoneEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
