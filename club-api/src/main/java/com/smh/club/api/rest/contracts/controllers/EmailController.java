package com.smh.club.api.rest.contracts.controllers;

import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.rest.dto.EmailDto;
import org.springframework.http.ResponseEntity;

public interface EmailController {
    ResponseEntity<PageResponse<EmailDto>> page(int pageNumber, int pageSize, String sortDir, String sort);
    ResponseEntity<EmailDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<EmailDto> create(EmailDto emailDto);
    ResponseEntity<EmailDto> update(int id, EmailDto emailDto);
    ResponseEntity<Void> delete(int id);
}
