package com.smh.club.api.common.controllers;

import com.smh.club.data.dto.EmailDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface EmailController {
    ResponseEntity<PageResponse<EmailDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<EmailDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<EmailDto> create(EmailDto emailDto);
    ResponseEntity<EmailDto> update(int id, EmailDto emailDto);
    ResponseEntity<Void> delete(int id);
}
