package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.*;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.data.dto.MemberDetailDto;
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
    private final AddressMapper addressMapper;
    private final EmailMapper emailMapper;
    private final PhoneMapper phoneMapper;
    private final RenewalMapper renewalMapper;

    private final Map<String, String> sortColumnMap = initSortColumnMap();


    @Override
    public PageResponse<MemberDto> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting member item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = membersRepo.findAll(pageRequest);

        return PageResponse.<MemberDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(memberMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Optional<MemberDto> getItem(int id) {
        log.debug("Getting member by id: {}", id);

        return membersRepo.findById(id).map(memberMapper::toDataObject);
    }

    @Override
    public MemberDto createItem(MemberDto member) {
        log.debug("creating member: {}", member);

        var memberEntity = memberMapper.toEntity(member);
        return memberMapper.toDataObject(membersRepo.save(memberEntity));
    }

    @Override
    public Optional<MemberDto> updateItem(int id, MemberDto memberDto) {
        log.debug("Updating member id: {}, with data: {}", id, memberDto);

        if(id != memberDto.getId()) {
            throw new IllegalArgumentException();
        }

        return membersRepo.findById(id)
                .map(e -> memberMapper.updateEntity(memberDto, e))
                .map(memberMapper::toDataObject);
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
    public Optional<MemberDetailDto> getMemberDetail(int id) {
        log.debug("Getting member detail by id: {}", id);

        var ret = membersRepo.findById(id);
        
        if (ret.isPresent()) {
            var entity = ret.get();
            var detail = memberMapper.toMemberDetail(entity);
            detail.setAddresses(addressMapper.toDataObjectList(entity.getAddresses()));
            detail.setEmails(emailMapper.toDataObjectList(entity.getEmails()));
            detail.setPhones(phoneMapper.toDataObjectList(entity.getPhones()));
            detail.setRenewals(renewalMapper.toDataObjectList(entity.getRenewals()));
            return Optional.of(detail);
        } else {
            return Optional.empty();
        }
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
