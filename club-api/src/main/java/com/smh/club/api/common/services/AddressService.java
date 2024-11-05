package com.smh.club.api.common.services;

import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface AddressService {
    PageResponse<AddressDto> getAddressListPage(PageParams pageParams);
    Optional<AddressDto> getAddress(int id);
    AddressDto createAddress(AddressCreateDto address);
    Optional<AddressDto> updateAddress(int id, AddressCreateDto address);
    void deleteAddress(int id);
    CountResponse getAddressCount();
}
