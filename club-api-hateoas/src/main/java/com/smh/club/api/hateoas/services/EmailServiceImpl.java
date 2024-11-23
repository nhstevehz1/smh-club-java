package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.domain.repos.EmailRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.contracts.assemblers.EmailAssembler;
import com.smh.club.api.hateoas.contracts.mappers.EmailMapper;
import com.smh.club.api.hateoas.contracts.services.EmailService;
import com.smh.club.api.hateoas.models.EmailModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class EmailServiceImpl extends AbstractServiceBase implements EmailService {

    private final EmailRepo emailRepo;

    private final MembersRepo membersRepo;

    private final EmailAssembler assembler;

    private final EmailMapper mapper;

    @Override
    public PagedModel<EmailModel> getEmailListPage(int pageNumber, int pageSize, String direction, String sort) {

        var pageRequest = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.fromString(direction),
            getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return assembler.toPagedModel(emailRepo.findAll(pageRequest));
    }

    @Override
    public Optional<EmailModel> getEmail(int id) {
        log.debug("Getting email by id: {}", id);
        return emailRepo.findById(id).map(assembler::toModel);
    }

    @Override
    public EmailModel createEmail(EmailModel email) {
        log.debug("Creating email: {}", email);

        var member = membersRepo.getReferenceById(email.getMemberId());
        var entity = mapper.toEntity(email);
        entity.setMember(member);

        return assembler.toModel(emailRepo.save(entity));
    }

    @Override
    public Optional<EmailModel> updateEmail(int id, EmailModel email) {
        log.debug("Updating email id: {}, with data: {}", id, email);

        return emailRepo.findByIdAndMemberId(id, email.getMemberId())
            .map(entity -> mapper.updateEntity(email, entity))
            .map(assembler::toModel);
    }

    @Override
    public void deleteEmail(int id) {
        log.debug("Deleting email id: {}", id);
        emailRepo.deleteById(id);
    }

    @Override
    public long getEmailCount() {
        return emailRepo.count();
    }

    @Override
    protected String getSortColumn(String key) {
        return getSort(key, EmailModel.class, EmailEntity.class).orElse("id");
    }
}
