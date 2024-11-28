package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.controllers.RenewalController;
import com.smh.club.api.hateoas.contracts.services.RenewalService;
import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.config.PagingConfig;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v2/renewals", produces = MediaTypes.HAL_JSON_VALUE)
public class RenewalControllerImpl implements RenewalController {

    private final RenewalService renewalService;

    /**
     * {@inheritDoc}
     */
    @GetMapping
    @Override
    public ResponseEntity<PagedModel<RenewalModel>> page(
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

        var page = renewalService.getRenewalListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(page);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("{id}")
    @Override
    public ResponseEntity<RenewalModel> get(@PathVariable int id) {
        log.debug("Getting renewal with id: {}", id);

        var ret = renewalService.getRenewal(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(renewalService.getRenewalCount()));
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<RenewalModel> create(@RequestBody RenewalModel renewal) {
        log.debug("Creating renewal, data: {}", renewal);

        var created = renewalService.createRenewal(renewal);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<RenewalModel> update(@PathVariable int id, @RequestBody RenewalModel renewal) {
        log.debug("Updating renewal, id: {}, data: {}", id, renewal);

        renewal.setId(0);
        var ret = renewalService.updateRenewal(id, renewal);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting renewal, id: {}", id);
        renewalService.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
