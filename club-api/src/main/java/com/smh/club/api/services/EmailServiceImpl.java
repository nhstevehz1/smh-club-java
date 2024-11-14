package com.smh.club.api.services;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.repos.EmailRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.CreateEmailDto;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class EmailServiceImpl extends AbstractServiceBase implements EmailService {
    
    private final EmailRepo emailRepo;
    private final MembersRepo memberRepo;
    private final EmailMapper emailMapper;


    @Override
    public PageResponse<EmailDto> getEmailListPage(@NonNull PageParams pageParams) {
        log.debug("Getting emil item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                getSortColumn(pageParams.getSortColumn()));

        log.debug("Created pageable: {}", pageRequest);

        var page = emailRepo.findAll(pageRequest);

        return PageResponse.<EmailDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(emailMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<EmailDto> getEmail(int id) {
        log.debug("Getting email by id: {}", id);

        return emailRepo.findById(id).map(emailMapper::toDto);
    }

    @Override
    public EmailDto createEmail(CreateEmailDto createDto) {
        log.debug("creating email: {}", createDto);

        var memberRef = memberRepo.getReferenceById(createDto.getMemberId());
        var emailEntity = emailMapper.toEntity(createDto);
        emailEntity.setMember(memberRef);
        return emailMapper.toDto(emailRepo.save(emailEntity));
    }

    @Override
    public Optional<EmailDto> updateEmail(int id, CreateEmailDto updateDto) {
        log.debug("Updating email, id: {}, with data: {}", id, updateDto);

        return emailRepo.findByIdAndMemberId(id, updateDto.getMemberId())
                .map(e -> emailMapper.updateEntity(updateDto, e))
                .map(emailMapper::toDto);
    }

    @Override
    public void deleteEmail(int id) {
        log.debug("Deleting address, id: {}", id);
        emailRepo.deleteById(id);
    }

    @Override
    public CountResponse getEmailCount() {
        log.debug("Getting member count");
        return CountResponse.of(emailRepo.count());
    }

    protected String getSortColumn(String key) {
        var source = EmailDto.class;
        var target = EmailEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
