package com.smh.club.api.data.services;

import com.smh.club.api.data.contracts.mappers.MemberMapper;
import com.smh.club.api.data.contracts.services.MemberService;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.dto.MemberDetailDto;
import com.smh.club.api.data.dto.MemberDto;
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
public class MemberServiceImpl extends AbstractServiceBase implements MemberService {

    private final MembersRepo membersRepo;
    private final MemberMapper memberMapper;

    @Override
    public Page<MemberDto> getMemberListPage(int pageNumber, int pageSize,
                                             @NonNull String direction, @NonNull String sort) {

        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.fromString(direction),
                getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return membersRepo.findAll(pageRequest).map(memberMapper::toDto);
    }

    @Override
    public Optional<MemberDto> getMember(int id) {
        log.debug("Getting member by id: {}", id);

        return membersRepo.findById(id).map(memberMapper::toDto);
    }

    @Override
    public MemberDto createMember(MemberDto member) {
        log.debug("creating member: {}", member);

        var memberEntity = memberMapper.toEntity(member);
        return memberMapper.toDto(membersRepo.save(memberEntity));
    }


    @Override
    public Optional<MemberDto> updateMember(int id, MemberDto member) {
        log.debug("Updating member id: {}, with data: {}", id, member);

        return membersRepo.findById(id)
                .map(e -> memberMapper.updateEntity(member, e))
                .map(memberMapper::toDto);
    }

    @Override
    public void deleteMember(int id) {
        log.debug("Deleting member id: {}", id);
        membersRepo.deleteById(id);
    }

    @Override
    public long getMemberCount() {
        log.debug("Getting member count");
        return membersRepo.count();
    }

    @Override
    public Optional<MemberDetailDto> getMemberDetail(int id) {
        log.debug("Getting member detail by id: {}", id);

        return membersRepo.findById(id)
                .map(memberMapper::toMemberDetailDto);
    }

    protected String getSortColumn(String key) {
        var source = MemberDto.class;
        var target = MemberEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("memberNumber"));
    }
}
