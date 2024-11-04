package com.smh.club.api.common.services;

import com.smh.club.api.dto.MemberMinimumDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;


public interface MemberService {
    PageResponse<MemberMinimumDto> getItemListPage(PageParams pageParams);
    Optional<MemberMinimumDto> getItem(int id);
    MemberMinimumDto createItem(MemberMinimumDto item);
    Optional<MemberMinimumDto> updateItem(int id, MemberMinimumDto memberMinimumDto);
    void deleteItem(int id);
    CountResponse getItemCount();
    Optional<MemberDto> getMemberDetail(int id);
}
