package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.services.RenewalService;
import com.smh.club.api.hateoas.models.RenewalModel;
import com.smh.club.api.hateoas.response.CountResponse;
import com.smh.club.api.hateoas.validation.constraints.SortConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Defines REST endpoints that targets renewal objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v2/renewals", produces = MediaTypes.HAL_JSON_VALUE)
public class RenewalController {

    private final String DEFAULT_SORT = "id";

    private final RenewalService renewalService;

    /**
     * Endpoint for retrieving a page of renewal objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link RenewalModel}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping
    public ResponseEntity<PagedModel<RenewalModel>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(RenewalModel.class)
        Pageable pageable) {

        log.debug("Getting member page for pageable:  {}", pageable );

        var page = renewalService.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single renewal from the database.
     *
     * @param id The id of the renewal.
     * @return @return A {@link ResponseEntity} containing a {@link RenewalModel}
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}")
    public ResponseEntity<RenewalModel> get(@PathVariable int id) {
        log.debug("Getting renewal with id: {}", id);

        var ret = renewalService.getRenewal(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of addresses in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(renewalService.getRenewalCount()));
    }

    /**
     * Endpoint for creating a renewal.
     *
     * @param renewal The {@link RenewalModel } used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link RenewalModel} representing the newly created object.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenewalModel> create(
        @NotNull @Valid @RequestBody RenewalModel renewal) {
        log.debug("Creating renewal, data: {}", renewal);

        var created = renewalService.createRenewal(renewal);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * Endpoint for updating a renewal.
     *
     * @param id The id of the renewal to update in the database.
     * @param renewal The {@link RenewalModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link RenewalModel} that represents the updated renewal.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenewalModel> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody RenewalModel renewal) {

        log.debug("Updating renewal, id: {}, data: {}", id, renewal);

        renewal.setId(0);
        var ret = renewalService.updateRenewal(id, renewal);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a renewal from the database.
     *
     * @param id The id of the renewal to delete
     * @return an empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting renewal, id: {}", id);
        renewalService.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
