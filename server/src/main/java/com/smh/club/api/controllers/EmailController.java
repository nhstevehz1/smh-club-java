package com.smh.club.api.controllers;

import com.smh.club.api.contracts.services.EmailService;
import com.smh.club.api.dto.email.EmailCreateDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.email.EmailFullNameDto;
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
 * Defines REST endpoints that targets email objects in the database.
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailController {

    private final String DEFAULT_SORT = "id";

    private final EmailService emailSvc;

    /**
     * Endpoint for retrieving a page of address objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a page of {@link EmailFullNameDto}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("page")
    public ResponseEntity<PagedDto<EmailFullNameDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(EmailFullNameDto.class)
        Pageable pageable) {

        var page = emailSvc.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single email from the database.
     *
     * @param id The id of the email.
     * @return @return A {@link ResponseEntity} containing a {@link EmailDto}
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}")
    public ResponseEntity<EmailDto> get(@PathVariable int id) {
        var ret = emailSvc.getEmail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of emails in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(emailSvc.getEmailCount()));
    }

    /**
     * Endpoint for creating an email.
     *
     * @param email The {@link EmailCreateDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link EmailDto} representing the newly created object.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PostMapping
    public ResponseEntity<EmailDto> create(
        @NotNull @Valid @RequestBody EmailCreateDto email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createEmail(email));
    }

    /**
     * Endpoint for updating an email.
     *
     * @param id The id of the email to update in the database.
     * @param email The {@link EmailDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link EmailDto} that represents the updated email.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping("{id}")
    public ResponseEntity<EmailDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody EmailDto email) {

        var ret = emailSvc.updateEmail(id, email);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting an email from the database.
     *
     * @param id The id of the email to delete
     * @return an empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        emailSvc.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
