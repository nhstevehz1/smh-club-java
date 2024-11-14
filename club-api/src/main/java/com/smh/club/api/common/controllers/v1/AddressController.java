package com.smh.club.api.common.controllers.v1;

import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.CreateAddressDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface AddressController {
    ResponseEntity<PageResponse<AddressDto>> page(int page, int size, String sortDir, String sort);
    ResponseEntity<AddressDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<AddressDto> create(CreateAddressDto address);
    ResponseEntity<AddressDto> update(int id, CreateAddressDto address);
    ResponseEntity<Void> delete(int id);
}
