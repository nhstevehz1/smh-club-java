package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.MemberService;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PagedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.smh.club.api.shared.annotations.SortConstraint;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final String DEFAULT_SORT = "member-number";

    private final MemberService memberSvc;

    /**
     * Endpoint for retrieving a page of member objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing an {@link PagedDto} of type {@link MemberDto}.
     */
    @GetMapping
    public ResponseEntity<PagedDto<MemberDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(MemberDto.class)
        Pageable pageable) {

        var page = memberSvc.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single member from the database.
     *
     * @param id The id of the member.
     * @return @return A {@link ResponseEntity} containing a {@link MemberDto}
     */
    @GetMapping("{id}")
    public ResponseEntity<MemberDto> get(@PathVariable int id) {
        var ret = memberSvc.getMember(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of members in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(memberSvc.getMemberCount()));
    }

    /**
     * Endpoint for creating a member.
     *
     * @param member The {@link MemberDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link MemberDto} representing the newly created object.
     */
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> create(@RequestBody MemberDto member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberSvc.createMember(member));
    }

    /**
     * Endpoint for updating a member.
     *
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link MemberDto} that represents the updated member.
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> update(@PathVariable int id, @RequestBody MemberDto member) {
        var ret = memberSvc.updateMember(id,  member);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a member from the database.
     *
     * @param id The id of the member to delete
     * @return ab empty {@link ResponseEntity}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("{id}/detail")
    public ResponseEntity<MemberDetailDto> detail(@PathVariable int id) {
        var ret = memberSvc.getMemberDetail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
