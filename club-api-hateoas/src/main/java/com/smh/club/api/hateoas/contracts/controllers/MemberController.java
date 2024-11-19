package com.smh.club.api.hateoas.contracts.controllers;

import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.hateoas.response.CountResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    ResponseEntity<PagedModel<EntityModel<MemberDto>>> page(int pageNumber, int pageSize, String sortDir, String sort);

    ResponseEntity<EntityModel<MemberDto>> get( int id);

    ResponseEntity<CountResponse> count();

    ResponseEntity<EntityModel<MemberDto>> create(MemberDto member);

    ResponseEntity<EntityModel<MemberDto>> update(int id, MemberDto member);

    ResponseEntity<Void> delete( int id);
    
}
