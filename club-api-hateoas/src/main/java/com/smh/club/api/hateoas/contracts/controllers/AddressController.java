package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface AddressController {

    ResponseEntity<PagedModel<AddressModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<AddressModel> get(int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<AddressModel> create(AddressModel address);

    ResponseEntity<AddressModel> update(int id, AddressModel address);

    ResponseEntity<Void> delete(int id);
}
