package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.*;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.MemberMinimumDto;
import com.smh.club.api.dto.MemberDto;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    private final MembersRepo membersRepo;

    private final MemberMapper memberMapper;

    private final Map<String, String> sortColumnMap = initSortColumnMap();


    @Override
    public PageResponse<MemberMinimumDto> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting member item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = membersRepo.findAll(pageRequest);

        return PageResponse.<MemberMinimumDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(memberMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<MemberMinimumDto> getItem(int id) {
        log.debug("Getting member by id: {}", id);

        return membersRepo.findById(id).map(memberMapper::toDto);
    }

    @Override
    public MemberMinimumDto createItem(MemberMinimumDto member) {
        log.debug("creating member: {}", member);

        var memberEntity = memberMapper.toEntity(member);
        return memberMapper.toDto(membersRepo.save(memberEntity));
    }


    @Override
    public Optional<MemberMinimumDto> updateItem(int id, MemberMinimumDto memberMinimumDto) {
        log.debug("Updating member id: {}, with data: {}", id, memberMinimumDto);

        if(id != memberMinimumDto.getId()) {
            throw new IllegalArgumentException();
        }

        return membersRepo.findById(id)
                .map(e -> memberMapper.updateEntity(memberMinimumDto, e))
                .map(memberMapper::toDto);
    }

    @Override
    public void deleteItem(int id) {
        log.debug("Deleting member id: {}", id);
        membersRepo.deleteById(id);
    }

    @Override
    public CountResponse getItemCount() {
        log.debug("Getting member count");
        return CountResponse.of(membersRepo.count());
    }

    @Override
    public Optional<MemberDto> getMemberDetail(int id) {
        log.debug("Getting member detail by id: {}", id);

        return membersRepo.findById(id)
                .map(memberMapper::toMemberDto);
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
