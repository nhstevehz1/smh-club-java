package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface AddressController {

    ResponseEntity<PagedModel<AddressModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<AddressModel> get(int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<AddressModel> create(MemberModel member);

    ResponseEntity<AddressModel> update(int id, MemberModel member);

    ResponseEntity<Void> delete(int id);
}
