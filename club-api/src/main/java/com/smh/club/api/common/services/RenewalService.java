package com.smh.club.api.common.services;

import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface RenewalService {
    PageResponse<RenewalDto> getItemListPage(PageParams pageParams);

    Optional<RenewalDto> getItem(int id);

    RenewalDto createItem(CreateRenewalDto renewal);

    Optional<RenewalDto> updateItem(int id, UpdateRenewalDto renewalDto);

    void deleteItem(int id);

    CountResponse getItemCount();
}
