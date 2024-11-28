package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.EmailService;
import com.smh.club.api.rest.dto.EmailDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.config.PagingConfig;

/**
 * Defines REST endpoints that targets email objects in the database.
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailController {
    
    private final EmailService emailSvc;

    /**
     * Endpoint for retrieving a page of email objects from the database.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing an {@link EmailDto}.
     */
    @GetMapping
    public ResponseEntity<PageResponse<EmailDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var page = emailSvc.getEmailListPage(pageNumber, pageSize, sortDir, sort);
        return ResponseEntity.ok(PageResponse.of(page));
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
    public ResponseEntity<EmailDto> create(@RequestBody EmailDto email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createEmail(email));
    }

    /**
     * Endpoint for updating an email.
     * @param id The id of the email to update in the database.
     * @param email The {@link EmailDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link EmailDto} that represents the updated email.
     */
    @PutMapping("{id}")
    public ResponseEntity<EmailDto> update(@PathVariable int id, @RequestBody EmailDto email) {
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
