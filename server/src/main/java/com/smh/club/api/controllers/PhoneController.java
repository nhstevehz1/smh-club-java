package com.smh.club.api.controllers;

import com.smh.club.api.contracts.services.PhoneService;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneMemberDto;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PagedDto;
import com.smh.club.api.validation.constraints.SortConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Defines REST endpoints that targets phone objects in the database.
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneController {

    private final String DEFAULT_SORT = "id";

    private final PhoneService phoneSvc;

    /**
     * Endpoint for retrieving a page of address objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a page of {@link PhoneMemberDto}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("page")
    public ResponseEntity<PagedDto<PhoneMemberDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(PhoneMemberDto.class)
        Pageable pageable) {

        var page = phoneSvc.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single phone from the database.
     *
     * @param id The id of the phone.
     * @return @return A {@link ResponseEntity} containing a {@link PhoneDto}
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}")
    public ResponseEntity<PhoneDto> get(@PathVariable int id) {
        var ret = phoneSvc.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of phones in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {

        return ResponseEntity.ok(CountResponse.of(phoneSvc.getPhoneCount()));
    }

    /**
     * Endpoint for creating a phone.
     *
     * @param phone The {@link PhoneDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} representing the newly created object.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('permission:write')")
    public ResponseEntity<PhoneDto> create(
        @NotNull @Valid @RequestBody PhoneDto phone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneSvc.createPhone(phone));
    }

    /**
     * Endpoint for updating a phone.
     *
     * @param id The id of the phone to update in the database.
     * @param phone The {@link PhoneDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} that represents the updated phone.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping("{id}")
    public ResponseEntity<PhoneDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody PhoneDto phone) {

        var ret = phoneSvc.updatePhone(id, phone);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a phone from the database.
     *
     * @param id The id of the phone to delete
     * @return an empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        phoneSvc.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
