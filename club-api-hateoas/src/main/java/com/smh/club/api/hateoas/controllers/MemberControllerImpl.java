package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.controllers.MemberController;
import com.smh.club.api.hateoas.contracts.services.MemberService;
import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.config.PagingConfig;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v2/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberControllerImpl implements MemberController {

    private final MemberService memberSvc;

    /**
     * {@inheritDoc}
     */
    @GetMapping
    @Override
    public ResponseEntity<PagedModel<MemberModel>> page(
        @RequestParam(value = PagingConfig.PAGE_NAME,
            defaultValue = "${request.paging.page}") int pageNumber,
        @RequestParam(value = PagingConfig.SIZE_NAME,
            defaultValue = "${request.paging.size}") int pageSize,
        @RequestParam(value = PagingConfig.DIRECTION_NAME,
            defaultValue = "${request.paging.direction}") String sortDir,
        @RequestParam(value = PagingConfig.SORT_NAME,
            defaultValue = "") String sort) {

        log.debug("Getting page. page: {}, size: {}, direction: {}, sort: {}",
            pageNumber, pageSize, sortDir, sort);

        var page = memberSvc.getMemberListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(page);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("{id}")
    @Override
    public ResponseEntity<MemberModel> get(@PathVariable int id) {
        log.debug("Getting Member with id: {}", id);
        var ret = memberSvc.getMember(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(memberSvc.getMemberCount()));
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<MemberModel> create(@RequestBody MemberModel member) {
        log.debug("Creating member data: {}", member);

        var created = memberSvc.createMember(member);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<MemberModel> update(@PathVariable int id, @RequestBody MemberModel member) {
        log.debug("Updating member id: {}, data: {} ", id, member);

        member.setId(id); // assume the path variable is the source of truth.
        var ret = memberSvc.updateMember(id, member);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting member id: {}", id);

        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
