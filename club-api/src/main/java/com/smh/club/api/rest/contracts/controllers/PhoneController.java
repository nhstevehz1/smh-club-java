package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.data.dto.PhoneDto;
import org.springframework.http.ResponseEntity;

public interface PhoneController {
    ResponseEntity<PageResponse<PhoneDto>> page(int pageNumber, int pageSize, String sortDir, String sort);
    ResponseEntity<PhoneDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<PhoneDto> create(PhoneDto createDto);
    ResponseEntity<PhoneDto> update(int id, PhoneDto updateDto);
    ResponseEntity<Void> delete(int id);
}
