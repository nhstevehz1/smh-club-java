package com.smh.club.api.data.contracts.services;

import com.smh.club.api.data.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AddressService {
    Page<AddressDto> getAddressListPage(int pageNumber, int pageSize,
                                        @NonNull String direction, @NonNull String sort);
    Optional<AddressDto> getAddress(int id);
    AddressDto createAddress(AddressDto address);
    Optional<AddressDto> updateAddress(int id, AddressDto addressDto);
    void deleteAddress(int id);
    long getAddressCount();
}
