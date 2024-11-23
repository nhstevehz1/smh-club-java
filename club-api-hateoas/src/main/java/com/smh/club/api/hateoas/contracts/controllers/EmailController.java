package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.EmailModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface EmailController {

    ResponseEntity<PagedModel<EmailModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<EmailModel> get(int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<EmailModel> create(EmailModel email);

    ResponseEntity<EmailModel> update(int id, EmailModel email);

    ResponseEntity<Void> delete(int id);
}
