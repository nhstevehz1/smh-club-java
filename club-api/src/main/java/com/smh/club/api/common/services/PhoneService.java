package com.smh.club.api.common.services;

import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface PhoneService {
    PageResponse<PhoneDto> getItemListPage(PageParams pageParams);

    Optional<PhoneDto> getItem(int id);

    PhoneDto createItem(CreatePhoneDto createDto);

    Optional<PhoneDto> updateItem(int id, UpdatePhoneDto updateDto);

    void deleteItem(int id);

    CountResponse getItemCount();
}
