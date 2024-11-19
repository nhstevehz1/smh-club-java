package com.smh.club.api.rest.contracts.services;

import com.smh.club.api.rest.dto.PhoneDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PhoneService {
    Page<PhoneDto> getPhoneListPage(int pageSize, int pageNumber, String direction, String sort);
    Optional<PhoneDto> getPhone(int id);
    PhoneDto createPhone(PhoneDto createDto);
    Optional<PhoneDto> updatePhone(int id, PhoneDto updateDto);
    void deletePhone(int id);
    long getPhoneCount();
}
