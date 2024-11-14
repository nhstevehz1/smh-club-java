package com.smh.club.api.common.services;

import com.smh.club.api.dto.CreateRenewalDto;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface RenewalService {
    PageResponse<RenewalDto> getRenewalListPage(PageParams pageParams);

    Optional<RenewalDto> getRenewal(int id);

    RenewalDto createRenewal(CreateRenewalDto renewal);

    Optional<RenewalDto> updateRenewal(int id, CreateRenewalDto renewalDto);

    void deleteRenewal(int id);

    CountResponse getRenewalCount();
}
