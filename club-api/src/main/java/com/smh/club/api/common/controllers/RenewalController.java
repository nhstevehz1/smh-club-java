package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RenewalController {

    ResponseEntity<PageResponse<RenewalDto>> getItemListPage(int page, int size, String sortDir, String sort);

    ResponseEntity<RenewalDto> getItem(int id);

    ResponseEntity<CountResponse> getCount();

    ResponseEntity<RenewalDto> createItem(CreateRenewalDto createDto);

    ResponseEntity<RenewalDto> updateItem(int id, UpdateRenewalDto updateDto);

    ResponseEntity<Void> deleteItem(int id);
}
