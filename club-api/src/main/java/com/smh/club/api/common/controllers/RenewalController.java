package com.smh.club.api.common.controllers;

import com.smh.club.data.dto.RenewalDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface RenewalController {
    ResponseEntity<PageResponse<RenewalDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<RenewalDto> get(int id);
    ResponseEntity<CountResponse> get();
    ResponseEntity<RenewalDto> create(RenewalDto createDto);
    ResponseEntity<RenewalDto> update(int id, RenewalDto updateDto);
    ResponseEntity<Void> delete(int id);
}
