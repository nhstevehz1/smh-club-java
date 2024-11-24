package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.controllers.EmailController;
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
import smh.club.shared.config.PagingConfig;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailControllerImpl implements EmailController {
    
    private final EmailService emailSvc;

    /**
     * {@inheritDoc}
     */
    @GetMapping
    @Override
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
     * {@inheritDoc}
     */
    @GetMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> get(@PathVariable int id) {
        var ret = emailSvc.getEmail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(emailSvc.getEmailCount()));
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping
    @Override
    public ResponseEntity<EmailDto> create(@RequestBody EmailDto email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createEmail(email));
    }

    /**
     * {@inheritDoc}
     */
    @PutMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> update(@PathVariable int id, @RequestBody EmailDto email) {
        var ret = emailSvc.updateEmail(id, email);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        emailSvc.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
