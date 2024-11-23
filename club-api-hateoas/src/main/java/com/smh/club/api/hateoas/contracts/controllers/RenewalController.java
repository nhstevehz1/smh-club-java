package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface RenewalController {

    ResponseEntity<PagedModel<RenewalModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<RenewalModel> get(int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<RenewalModel> create(RenewalModel email);

    ResponseEntity<RenewalModel> update(int id, RenewalModel email);

    ResponseEntity<Void> delete(int id);
}
