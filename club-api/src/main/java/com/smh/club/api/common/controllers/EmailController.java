package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.dto.update.UpdateEmailDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface EmailController {
    ResponseEntity<PageResponse<EmailDto>> getItemListPage(int page, int size, String sortDir, String sort);
    ResponseEntity<EmailDto> getItem(int id);
    ResponseEntity<CountResponse> getCount();
    ResponseEntity<EmailDto> createItem(CreateEmailDto emailDto);
    ResponseEntity<EmailDto> updateItem(int id, UpdateEmailDto emailDto);
    ResponseEntity<Void> deleteItem(int id);
}
