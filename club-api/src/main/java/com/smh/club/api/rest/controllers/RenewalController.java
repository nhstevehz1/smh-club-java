package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.RenewalService;
import com.smh.club.api.rest.dto.PhoneDto;
import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PagedDto;
import com.smh.club.api.shared.validators.constraints.SortConstraint;
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
 * Defines REST endpoints that targets renewal objects in the database.
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v1/renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalController {

    private final String DEFAULT_SORT = "id";

    private final RenewalService renewSvc;

    /**
     * Endpoint for retrieving a page of renewal objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a page of {@link PhoneDto}.
     */
    @GetMapping
    public ResponseEntity<PagedDto<RenewalDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(RenewalDto.class)
        Pageable pageable) {

        var page = renewSvc.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single renewal from the database.
     *
     * @param id The id of the renewal.
     * @return @return A {@link ResponseEntity} containing a {@link RenewalDto}
     */
    @GetMapping("{id}")
    public ResponseEntity<RenewalDto> get(@PathVariable int id) {
        var ret = renewSvc.getRenewal(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of addresses in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping("count")
    public ResponseEntity<CountResponse> get() {

        return ResponseEntity.ok(CountResponse.of(renewSvc.getRenewalCount()));
    }

    /**
     * Endpoint for creating a renewal.
     *
     * @param renewal The {@link RenewalDto } used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} representing the newly created object.
     */
    @PostMapping
    public ResponseEntity<RenewalDto> create(
        @NotNull @Valid @RequestBody RenewalDto renewal) {

        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createRenewal(renewal));
    }

    /**
     * Endpoint for updating a renewal.
     *
     * @param id The id of the renewal to update in the database.
     * @param renewal The {@link RenewalDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} that represents the updated renewal.
     */
    @PutMapping("{id}")
    public ResponseEntity<RenewalDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody RenewalDto renewal) {

        var ret = renewSvc.updateRenewal(id, renewal);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a renewal from the database.
     *
     * @param id The id of the renewal to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        renewSvc.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
