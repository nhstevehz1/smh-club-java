package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.EmailService;
import com.smh.club.api.rest.dto.EmailDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PagedDto;
import com.smh.club.api.rest.validation.constraints.SortConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Defines REST endpoints that targets email objects in the database.
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
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
     * @return A {@link ResponseEntity} containing a page of {@link EmailDto}.
     */
    @GetMapping
    public ResponseEntity<PagedDto<EmailDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(EmailDto.class)
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
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(emailSvc.getEmailCount()));
    }

    /**
     * Endpoint for creating an email.
     *
     * @param email The {@link EmailDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link EmailDto} representing the newly created object.
     */
    @PostMapping
    public ResponseEntity<EmailDto> create(
        @NotNull @Valid @RequestBody EmailDto email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createEmail(email));
    }

    /**
     * Endpoint for updating an email.
     * @param id The id of the email to update in the database.
     * @param email The {@link EmailDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link EmailDto} that represents the updated email.
     */
    @PutMapping("{id}")
    public ResponseEntity<EmailDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody EmailDto email) {

        var ret = emailSvc.updateEmail(id, email);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting an email from the database.
     * @param id The id of the email to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        emailSvc.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
