package com.smh.club.api.Services;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.mappers.IDataObjectMapper;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class MemberService implements IMemberService {

    private final MembersRepo membersRepo;
    private final IDataObjectMapper<Member, MemberEntity> memberMapper;

    private final Map<String, String> sortColumnMap;

    public MemberService(MembersRepo membersRepo, IDataObjectMapper<Member, MemberEntity> memberMapper) {
        log.debug("Creating Member Service");

        this.membersRepo = membersRepo;
        this.memberMapper = memberMapper;
        this.sortColumnMap = initSortColumnMap();
    }

    @Override
    public PageResponse<Member> getMemberPage(Optional<PageParams> pageParams) {
        log.debug("Getting member page, prams: " + (pageParams.isPresent() ? pageParams.get().toString() : "null"));

        var request = pageParams.orElse(PageParams.getDefault());
        request.setSortColumn(sortColumnMap.get(request.getSortColumn()));

        var pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                request.getSortDirection(),
                request.getSortColumn());

        var page = membersRepo.findAll(pageable);

        return PageResponse.<Member>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(memberMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public MemberDetail getMemberDetail(int id) {
        return null;
    }

    @Override
    public Member createMember(Member member) {
        var memberEntity = memberMapper.toEntity(member);
        return memberMapper.toDataObject(membersRepo.save(memberEntity));
    }

    @Override
    public Member updateMember(Member member) {
        var memberEntity = membersRepo.getReferenceById(member.getId());
        memberMapper.update(member, memberEntity);
        return memberMapper.toDataObject(memberEntity);
    }

    @Override
    public void deleteMember(int id) {
        membersRepo.deleteById(id);
    }

    @Override
    public CountResponse getMemberCount() {
        return new CountResponse(membersRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "memberNumber");
        map.put("member-number", "memberNumber");
        map.put("first-name", "firstName");
        map.put("middle-name", "middleName");
        map.put("last-name", "lastName");
        map.put("birth-date", "birthDate");
        map.put("joined-date", "joinedDate");

        return map;
    }
}
