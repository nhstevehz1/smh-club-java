package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.MemberMapper;
import com.smh.club.api.rest.contracts.services.MemberService;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.member.MemberCreateDto;
import com.smh.club.api.rest.dto.member.MemberDetailDto;
import com.smh.club.api.rest.dto.member.MemberDto;
import com.smh.club.api.rest.dto.member.MemberUpdateDto;
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
 * Extends an {@link AbstractServiceBase} and implements an {@link MemberService}.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl extends AbstractServiceBase implements MemberService {

    private final MembersRepo membersRepo;
    private final MemberMapper memberMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PagedDto<MemberDto> getPage(Pageable pageable) {

        var pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            getSort(pageable.getSort()));

        log.debug("Created pageable: {}", pageRequest);

        var page = memberMapper.toPage(membersRepo.findAll(pageRequest));

        return PagedDto.of(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberDto> getMember(int id) {
        log.debug("Getting member by id: {}", id);

        return membersRepo.findById(id).map(memberMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDto createMember(MemberCreateDto member) {
        log.info("creating member: {}", member);

        var memberEntity = memberMapper.toEntity(member);

        // will return empty optional if table is empty.
        var maxMemNum = membersRepo.findMaxMemberNumber().orElse(0);
        log.info("Max mem number: {}", maxMemNum);
        log.info("New member number: {}", maxMemNum + 1);
        memberEntity.setMemberNumber(maxMemNum + 1);
        return memberMapper.toDto(membersRepo.save(memberEntity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberDto> updateMember(int id, MemberUpdateDto member) {
        log.debug("Updating member id: {}, with data: {}", id, member);

        return membersRepo.findById(id)
                .map(e -> memberMapper.updateEntity(member, e))
                .map(memberMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMember(int id) {
        log.debug("Deleting member id: {}", id);
        membersRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMemberCount() {
        log.debug("Getting member count");
        return membersRepo.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberDetailDto> getMemberDetail(int id) {
        log.debug("Getting member detail by id: {}", id);

        return membersRepo.findById(id)
                .map(memberMapper::toMemberDetailDto);
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
                    getSort(o.getProperty(), MemberDto.class, MemberEntity.class)
                        .orElseThrow(IllegalArgumentException::new))).toList();

        return Sort.by(orders);
    }

}
