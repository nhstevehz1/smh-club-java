package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.EmailMapper;
import com.smh.club.api.rest.contracts.services.EmailService;
import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.domain.repos.EmailRepo;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.EmailDto;
import com.smh.club.api.rest.dto.EmailMemberDto;
import com.smh.club.api.rest.response.PagedDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements an {@link EmailService}.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class EmailServiceImpl extends AbstractServiceBase implements EmailService {
    
    private final EmailRepo emailRepo;
    private final MembersRepo memberRepo;
    private final EmailMapper emailMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedDto<EmailMemberDto> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = emailMapper.toPage(emailRepo.findAll(pageRequest));

        return PagedDto.of(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<EmailDto> getEmail(int id) {
        log.debug("Getting email by id: {}", id);

        return emailRepo.findById(id).map(emailMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailDto createEmail(EmailDto createDto) {
        log.debug("creating email: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var emailEntity = emailMapper.toEntity(createDto);
        emailEntity.setMember(memberRef);
        return emailMapper.toDto(emailRepo.save(emailEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<EmailDto> updateEmail(int id, EmailDto updateDto) {
        log.debug("Updating email, id: {}, with data: {}", id, updateDto);

        return emailRepo.findByIdAndMemberId(id, updateDto.getMemberId())
                .map(e -> emailMapper.updateEntity(updateDto, e))
                .map(emailMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmail(int id) {
        log.debug("Deleting address, id: {}", id);
        emailRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getEmailCount() {
        log.debug("Getting member count");
        return emailRepo.count();
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
                    getSort(o.getProperty(), EmailMemberDto.class, EmailEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
