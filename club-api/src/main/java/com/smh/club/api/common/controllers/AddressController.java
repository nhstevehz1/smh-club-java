package com.smh.club.api.common.controllers;

import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.dto.update.UpdateAddressDto;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AddressController {

    ResponseEntity<PageResponse<AddressDto>> getAddressListPage( int page, int size, String sortDir, String sort);

    ResponseEntity<AddressDto> getAddress(int id);

    ResponseEntity<CountResponse> getCount();

    ResponseEntity<AddressDto> createAddress(CreateAddressDto address);

    ResponseEntity<AddressDto> updateAddress(int id, UpdateAddressDto address);

    ResponseEntity<Void> deleteAddress(int id);
}
