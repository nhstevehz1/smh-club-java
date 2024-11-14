package com.smh.club.api.common.controllers.v1;

import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface PhoneController {
    ResponseEntity<PageResponse<PhoneDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<PhoneDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<PhoneDto> create(CreatePhoneDto createDto);
    ResponseEntity<PhoneDto> update(int id, CreatePhoneDto updateDto);
    ResponseEntity<Void> delete(int id);
}
