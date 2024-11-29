package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.services.MemberService;
import com.smh.club.api.hateoas.models.MemberModel;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.annotations.SortConstraint;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v2/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final String DEFAULT_SORT = "member-number";

    private final MemberService memberSvc;

    /**
     * Endpoint for retrieving a page of member objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing an {@link PagedModel} of type {@link MemberModel}.
     */
    @GetMapping
    public ResponseEntity<PagedModel<MemberModel>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(MemberModel.class)
        Pageable pageable) {

        log.debug("Getting member page for pageable:  {}", pageable );

        var page = memberSvc.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single member from the database.
     *
     * @param id The id of the member.
     * @return @return A {@link ResponseEntity} containing a {@link MemberModel}
     */
    @GetMapping("{id}")
    //@Override
    public ResponseEntity<MemberModel> get(@PathVariable int id) {
        log.debug("Getting Member with id: {}", id);
        var ret = memberSvc.getMember(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of members in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    //@Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(memberSvc.getMemberCount()));
    }

    /**
     * Endpoint for creating a member.
     *
     * @param member The {@link MemberModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link MemberModel} representing the newly created object.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    //@Override
    public ResponseEntity<MemberModel> create(@RequestBody MemberModel member) {
        log.debug("Creating member data: {}", member);

        var created = memberSvc.createMember(member);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * Endpoint for updating a member.
     *
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link MemberModel} that represents the updated member.
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@Override
    public ResponseEntity<MemberModel> update(@PathVariable int id, @RequestBody MemberModel member) {
        log.debug("Updating member id: {}, data: {} ", id, member);

        member.setId(id); // assume the path variable is the source of truth.
        var ret = memberSvc.updateMember(id, member);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a member from the database.
     *
     * @param id The id of the member to delete
     * @return ab empty {@link ResponseEntity}.
     */
    @DeleteMapping(value = "{id}")
    //@Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting member id: {}", id);

        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
