package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.CreateEmailDto;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface EmailController {
    ResponseEntity<PageResponse<EmailDto>> getEmailListPage(int page, int size, String sortDir, String sort);
    ResponseEntity<EmailDto> getItem(int id);
    ResponseEntity<CountResponse> getCount();
    ResponseEntity<EmailDto> createEmail(CreateEmailDto emailDto);
    ResponseEntity<EmailDto> updateEmail(int id, CreateEmailDto emailDto);
    ResponseEntity<Void> deleteEmail(int id);
}
