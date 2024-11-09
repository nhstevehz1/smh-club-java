package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface PhoneController {

    ResponseEntity<PageResponse<PhoneDto>> getPhoneListPage(int page, int size, String sortDir, String sort);

    ResponseEntity<PhoneDto> getPhone(int id);

    ResponseEntity<CountResponse> getCount();

    ResponseEntity<PhoneDto> createPhone(CreatePhoneDto createDto);

    ResponseEntity<PhoneDto> updatePhone(int id, UpdatePhoneDto updateDto);

    ResponseEntity<Void> deletePhone(int id);
}
