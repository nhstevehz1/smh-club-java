package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.controllers.PhoneController;
import com.smh.club.api.hateoas.contracts.services.PhoneService;
import com.smh.club.api.hateoas.models.PhoneModel;
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
@RequestMapping(value = "/api/v2/phones", produces = MediaTypes.HAL_JSON_VALUE)
public class PhoneControllerImpl implements PhoneController {

    private final PhoneService phoneService;

    /**
     * {@inheritDoc}
     */
    @GetMapping
    @Override
    public ResponseEntity<PagedModel<PhoneModel>> page(
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

        var page = phoneService.getPhoneListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(page);
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping("{id}")
    @Override
    public ResponseEntity<PhoneModel> get(@PathVariable int id) {
        log.debug("Getting phone with id: {}", id);

        var ret = phoneService.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(phoneService.getPhoneCount()));
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PhoneModel> create(@RequestBody PhoneModel phone) {
        log.debug("Creating phone, data: {}", phone);

        var created = phoneService.createPhone(phone);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PhoneModel> update(@PathVariable int id, @RequestBody PhoneModel phone) {
        log.debug("Updating phone, id: {}, data: {}", id, phone);

        phone.setId(0);
        var ret = phoneService.updatePhone(id, phone);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * {@inheritDoc}
     */
    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting phone, id: {}", id);
        phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
