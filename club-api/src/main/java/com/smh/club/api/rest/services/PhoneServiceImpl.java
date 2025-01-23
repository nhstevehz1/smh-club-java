package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.PhoneMapper;
import com.smh.club.api.rest.contracts.services.PhoneService;
import com.smh.club.api.rest.domain.entities.PhoneEntity;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.domain.repos.PhoneRepo;
import com.smh.club.api.rest.dto.PhoneDto;
import com.smh.club.api.rest.dto.PhoneMemberDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements an {@link PhoneService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PhoneServiceImpl extends AbstractServiceBase implements PhoneService {
    
    private final PhoneRepo phoneRepo;
    private final MembersRepo memberRepo;
    private final PhoneMapper phoneMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedDto<PhoneMemberDto> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = phoneMapper.toPage(phoneRepo.findAll(pageRequest));

        return PagedDto.of(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PhoneDto> getPhone(int id) {
        log.debug("Getting phone by id: {}", id);

        return phoneRepo.findById(id).map(phoneMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoneDto createPhone(PhoneDto createDto) {
        log.debug("creating phone: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var phoneEntity = phoneMapper.toEntity(createDto);
        phoneEntity.setMember(memberRef);
        return phoneMapper.toDto(phoneRepo.save(phoneEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PhoneDto> updatePhone(int id, PhoneDto updateDto) {
        log.debug("Updating phone, id: {}, with data: {}", id, updateDto);

        return phoneRepo.findByIdAndMemberId(id, updateDto.getMemberId())
                .map(e -> phoneMapper.updateEntity(updateDto, e))
                .map(phoneMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePhone(int id) {
        log.debug("Deleting address, id: {}", id);
        phoneRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPhoneCount() {
        log.debug("Getting member count");
        return phoneRepo.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Sort getSort(Sort sort) {
        if (sort.isUnsorted()) {
            return sort;
        }

        var orders =
            sort.get()
                .map(o -> new Sort.Order(o.getDirection(),
                    getSort(o.getProperty(), PhoneMemberDto.class, PhoneEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
