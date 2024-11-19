package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.data.dto.RenewalDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RenewalService {
    Page<RenewalDto> getRenewalListPage(int pageSize, int pageNumber, String direction, String sort);
    Optional<RenewalDto> getRenewal(int id);
    RenewalDto createRenewal(RenewalDto renewal);
    Optional<RenewalDto> updateRenewal(int id, RenewalDto renewalDto);
    void deleteRenewal(int id);
    long getRenewalCount();
}
