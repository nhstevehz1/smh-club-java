package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.services.EmailService;
import com.smh.club.api.hateoas.models.EmailModel;
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
 * Defines REST endpoints that targets email objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@Validated
@RequestMapping(value = "/api/v2/emails", produces = MediaTypes.HAL_JSON_VALUE)
public class EmailController {

    private final String DEFAULT_SORT = "id";

    private final EmailService emailService;

    /**
     * Endpoint for retrieving a page of email objects from the database.
     *
     * @param pageable A {@link Pageable} that contains the sort criteria.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link EmailModel}.
     */
    @GetMapping
    public ResponseEntity<PagedModel<EmailModel>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(EmailModel.class)
        Pageable pageable) {

        log.debug("Getting member page for pageable:  {}", pageable );

        var page = emailService.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single email from the database.
     *
     * @param id The id of the email.
     * @return @return A {@link ResponseEntity} containing a {@link EmailModel}
     */
    @GetMapping("{id}")
    public ResponseEntity<EmailModel> get(@PathVariable int id) {
        log.debug("Getting email with id: {}", id);

        var ret = emailService.getEmail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of emails in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(emailService.getEmailCount()));
    }

    /**
     * Endpoint for creating an email.
     *
     * @param email The {@link EmailModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link EmailModel} representing the newly created object.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailModel> create(@RequestBody EmailModel email) {
        log.debug("Creating email, data: {}", email);

        var created = emailService.createEmail(email);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * Endpoint for updating an email.
     *
     * @param id The id of the email to update in the database.
     * @param email The {@link EmailModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link EmailModel} that represents the updated email.
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailModel> update(@PathVariable int id, @RequestBody EmailModel email) {
        log.debug("Updating email, id: {}, data: {}", id, email);

        var ret = emailService.updateEmail(id, email);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting an email from the database.
     *
     * @param id The id of the email to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting email, id: {}", id);
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
