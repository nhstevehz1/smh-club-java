package com.smh.club.api.rest.services;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.domain.repos.EmailRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.rest.contracts.mappers.EmailMapper;
import com.smh.club.api.rest.contracts.services.EmailService;
import com.smh.club.api.rest.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smh.club.shared.services.AbstractServiceBase;

import java.util.Optional;

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
    public Page<EmailDto> getEmailListPage(int pageNumber, int pageSize,
                                           @NonNull String direction, @NonNull String sort) {

        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.fromString(direction),
                getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return emailRepo.findAll(pageRequest).map(emailMapper::toDto);
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
    protected String getSortColumn(String key) {
        var source = EmailDto.class;
        var target = EmailEntity.class;

        return getSort(key, source, target)
                .orElse("id");
    }
}
