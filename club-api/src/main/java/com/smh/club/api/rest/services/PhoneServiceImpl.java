package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.PhoneMapper;
import com.smh.club.api.rest.contracts.services.PhoneService;
import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.PhoneRepo;
import com.smh.club.api.data.dto.PhoneDto;
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
public class PhoneServiceImpl extends AbstractServiceBase implements PhoneService {
    
    private final PhoneRepo phoneRepo;
    private final MembersRepo memberRepo;
    private final PhoneMapper phoneMapper;

    @Override
    public Page<PhoneDto> getPhoneListPage(int pageNumber, int pageSize,
                                           @NonNull String direction, @NonNull String sort) {

        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.fromString(direction),
                getSortColumn(sort));
        
        log.debug("Created pageable: {}", pageRequest);

        return phoneRepo.findAll(pageRequest).map(phoneMapper::toDto);
    }

    @Override
    public Optional<PhoneDto> getPhone(int id) {
        log.debug("Getting phone by id: {}", id);

        return phoneRepo.findById(id).map(phoneMapper::toDto);
    }

    @Override
    public PhoneDto createPhone(PhoneDto createDto) {
        log.debug("creating phone: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var phoneEntity = phoneMapper.toEntity(createDto);
        phoneEntity.setMember(memberRef);
        return phoneMapper.toDto(phoneRepo.save(phoneEntity));
    }

    @Override
    public Optional<PhoneDto> updatePhone(int id, PhoneDto updateDto) {
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
    public long getPhoneCount() {
        log.debug("Getting member count");
        return phoneRepo.count();
    }

    protected String getSortColumn(String key) {
        var source = PhoneDto.class;
        var target = PhoneEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
