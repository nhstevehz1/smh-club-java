package com.smh.club.api.common.services;

import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface PhoneService {
    PageResponse<PhoneDto> getPhoneListPage(PageParams pageParams);

    Optional<PhoneDto> getPhone(int id);

    PhoneDto createPhone(PhoneDto createDto);

    Optional<PhoneDto> updatePhone(int id, PhoneDto updateDto);

    void deletePhone(int id);

    CountResponse getPhoneCount();
}
