package com.smh.club.api.controllers;

import com.smh.club.api.contracts.services.MemberService;
import com.smh.club.api.dto.member.MemberCreateDto;
import com.smh.club.api.dto.member.MemberDetailDto;
import com.smh.club.api.dto.member.MemberDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PagedDto;
import com.smh.club.api.validation.constraints.SortConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Defines REST endpoints that targets member objects in the database.
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final String DEFAULT_SORT = "memberNumber";

    private final MemberService memberSvc;

    /**
     * Endpoint for retrieving a page of member objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing an {@link PagedDto} of type {@link MemberDto}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
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
    @PreAuthorize("hasAuthority('permission:read')")
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
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(memberSvc.getMemberCount()));
    }

    /**
     * Endpoint for creating a member.
     *
     * @param member The {@link MemberCreateDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link MemberDto} representing the newly created object.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> create(
        @NotNull @Valid @RequestBody MemberCreateDto member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberSvc.createMember(member));
    }

    /**
     * Endpoint for updating a member.
     *
     * @param id The id of the member to update in the database.
     * @param member The {@link MemberDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link MemberDto} that represents the updated member.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody MemberDto member) {

        var ret = memberSvc.updateMember(id,  member);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a member from the database.
     *
     * @param id The id of the member to delete
     * @return ab empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('permission:write')")
    @GetMapping("{id}/detail")
    public ResponseEntity<MemberDetailDto> detail(@PathVariable int id) {
        var ret = memberSvc.getMemberDetail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
