package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.AddressModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface AddressService {
    PagedModel<AddressModel> getAddressListPage(int pageNumber, int pageSize, String direction, String sort);

    Optional<AddressModel> getAddress(int id);

    AddressModel createAddress(AddressModel address);

    Optional<AddressModel> updateAddress(int id, AddressModel address);

    void deleteAddress(int id);

    long getAddressCount();
}
