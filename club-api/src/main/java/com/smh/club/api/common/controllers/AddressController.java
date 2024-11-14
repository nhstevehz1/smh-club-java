package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.CreateAddressDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface AddressController {

    ResponseEntity<PageResponse<AddressDto>> getAddressListPage( int page, int size, String sortDir, String sort);

    ResponseEntity<AddressDto> getAddress(int id);

    ResponseEntity<CountResponse> getCount();

    ResponseEntity<AddressDto> createAddress(CreateAddressDto address);

    ResponseEntity<AddressDto> updateAddress(int id, CreateAddressDto address);

    ResponseEntity<Void> deleteAddress(int id);
}
