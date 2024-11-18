package com.smh.club.api.rest.contracts;

import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.data.dto.RenewalDto;
import org.springframework.http.ResponseEntity;

public interface RenewalController {
    ResponseEntity<PageResponse<RenewalDto>> page(int pageNumber, int pageSize, String sortDir, String sort);
    ResponseEntity<RenewalDto> get(int id);
    ResponseEntity<CountResponse> get();
    ResponseEntity<RenewalDto> create(RenewalDto createDto);
    ResponseEntity<RenewalDto> update(int id, RenewalDto updateDto);
    ResponseEntity<Void> delete(int id);
}
