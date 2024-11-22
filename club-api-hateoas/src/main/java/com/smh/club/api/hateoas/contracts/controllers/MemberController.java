package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<PagedModel<MemberModel>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<MemberModel> get( int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<MemberModel> create(MemberModel member);

    ResponseEntity<MemberModel> update(int id, MemberModel member);

    ResponseEntity<Void> delete( int id);
    
}
