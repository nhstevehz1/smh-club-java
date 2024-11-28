package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.MemberService;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.config.PagingConfig;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberSvc;

    /**
     * Endpoint for retrieving a page of member objects from the database.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link MemberDto}.
     */
    @GetMapping
    public ResponseEntity<PageResponse<MemberDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var page = memberSvc.getMemberListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(PageResponse.of(page));
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
