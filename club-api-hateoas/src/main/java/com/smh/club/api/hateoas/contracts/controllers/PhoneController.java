package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.PhoneModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface PhoneController {

    ResponseEntity<PagedModel<PhoneModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<PhoneModel> get(int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<PhoneModel> create(PhoneModel email);

    ResponseEntity<PhoneModel> update(int id, PhoneModel email);

    ResponseEntity<Void> delete(int id);
}
