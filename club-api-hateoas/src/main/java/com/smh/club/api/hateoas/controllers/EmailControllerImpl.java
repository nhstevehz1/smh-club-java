package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.config.PagingConfig;
import com.smh.club.api.hateoas.contracts.controllers.EmailController;
import com.smh.club.api.hateoas.contracts.services.EmailService;
import com.smh.club.api.hateoas.models.EmailModel;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v2/emails", produces = MediaTypes.HAL_JSON_VALUE)
public class EmailControllerImpl implements EmailController {

    private final EmailService emailService;

    /**
     * {@inheritDoc}
     */
    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EmailModel>> page(
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

        var page = emailService.getEmailListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(page);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("{id}")
    @Override
    public ResponseEntity<EmailModel> get(@PathVariable int id) {
        log.debug("Getting email with id: {}", id);

        var ret = emailService.getEmail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(emailService.getEmailCount()));
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EmailModel> create(@RequestBody EmailModel email) {
        log.debug("Creating email, data: {}", email);

        var created = emailService.createEmail(email);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EmailModel> update(@PathVariable int id, @RequestBody EmailModel email) {
        log.debug("Updating email, id: {}, data: {}", id, email);

        email.setId(0);
        var ret = emailService.updateEmail(id, email);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting email, id: {}", id);
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
