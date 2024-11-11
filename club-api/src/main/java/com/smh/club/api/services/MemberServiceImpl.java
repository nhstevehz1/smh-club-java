package com.smh.club.api.services;

import com.smh.club.api.configuration.ColumnSortMap;
import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.MemberCreateDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.factories.SortMapFactory;
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
public class MemberServiceImpl extends AbstractServiceBase implements MemberService {

    private final MembersRepo membersRepo;
    private final MemberMapper memberMapper;

    @Override
    public PageResponse<MemberDto> getMemberListPage(@NonNull PageParams pageParams) {
        log.debug("Getting member item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                getSortColumn(pageParams.getSortColumn()));

        log.debug("Created pageable: {}", pageRequest);

        var page = membersRepo.findAll(pageRequest);

        return PageResponse.<MemberDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(memberMapper.toMemberDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<MemberDto> getMember(int id) {
        log.debug("Getting member by id: {}", id);

        return membersRepo.findById(id).map(memberMapper::toMemberDto);
    }

    @Override
    public MemberDto createMember(MemberCreateDto member) {
        log.debug("creating member: {}", member);

        var memberEntity = memberMapper.toMemberEntity(member);
        return memberMapper.toMemberDto(membersRepo.save(memberEntity));
    }


    @Override
    public Optional<MemberDto> updateMember(int id, MemberCreateDto member) {
        log.debug("Updating member id: {}, with data: {}", id, member);

        return membersRepo.findById(id)
                .map(e -> memberMapper.updateMemberEntity(member, e))
                .map(memberMapper::toMemberDto);
    }

    @Override
    public void deleteMember(int id) {
        log.debug("Deleting member id: {}", id);
        membersRepo.deleteById(id);
    }

    @Override
    public CountResponse getMemberCount() {
        log.debug("Getting member count");
        return CountResponse.of(membersRepo.count());
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
                        .orElse("memberId"));
    }
}
