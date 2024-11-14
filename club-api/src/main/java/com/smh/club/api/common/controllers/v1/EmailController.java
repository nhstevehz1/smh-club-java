package com.smh.club.api.common.controllers.v1;

import com.smh.club.api.dto.CreateEmailDto;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface EmailController {
    ResponseEntity<PageResponse<EmailDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<EmailDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<EmailDto> create(CreateEmailDto emailDto);
    ResponseEntity<EmailDto> update(int id, CreateEmailDto emailDto);
    ResponseEntity<Void> delete(int id);
}
