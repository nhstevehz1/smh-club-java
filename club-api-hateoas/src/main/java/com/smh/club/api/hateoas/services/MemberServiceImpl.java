package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.contracts.assemblers.MemberAssembler;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.contracts.services.MemberService;
import com.smh.club.api.hateoas.models.MemberModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @Override
    public PagedModel<MemberModel> getMemberListPage(int pageNumber, int pageSize, String direction, String sort) {

        var pageRequest = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.fromString(direction),
            getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return assembler.toPagedModel(memberRepo.findAll(pageRequest));
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
    protected String getSortColumn(String key) {
        return getSort(key, MemberModel.class, MemberEntity.class).orElse("memberNumber");
    }
}
