package com.smh.club.api.hateoas.services;

import com.smh.club.api.hateoas.contracts.assemblers.MemberAssembler;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.contracts.services.MemberService;
import com.smh.club.api.hateoas.domain.entities.MemberEntity;
import com.smh.club.api.hateoas.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.models.MemberModel;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 * Extends an {@link AbstractServiceBase} and implements a {@link MemberService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl extends AbstractServiceBase implements MemberService {

    private final MembersRepo memberRepo;

    private final MemberAssembler assembler;

    private final MemberMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Valid
    @Override
    public PagedModel<MemberModel> getPage(Pageable pageable) {
        log.debug("Getting page for pageable: {}", pageable);

        var request = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort())); // map the dto field to the entity field.

        return assembler.toPagedModel(memberRepo.findAll(request));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberModel> getMember(int id) {
        log.debug("Getting member by id: {}", id);
        return memberRepo.findById(id).map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberModel createMember(MemberModel member) {
        log.debug("Creating member: {}", member);
        var entity = mapper.toEntity(member);
        return assembler.toModel(memberRepo.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberModel> updateMember(int id, MemberModel member) {
        log.debug("Updating member id: {}, with data: {}", id, member);

        return memberRepo.findById(id)
            .map(entity -> mapper.updateEntity(member, entity))
            .map(assembler::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMember(int id) {
        memberRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMemberCount() {
        return memberRepo.count();
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
                    getSort(o.getProperty(), MemberModel.class, MemberEntity.class)
                .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }
}
