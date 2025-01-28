package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.services.AddressService;
import com.smh.club.api.hateoas.models.AddressModel;
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
 * Defines REST endpoints that targets address objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v2/addresses", produces = MediaTypes.HAL_JSON_VALUE)
public class AddressController {

    private final String DEFAULT_SORT = "id";

    private final AddressService addressService;

    /**
     * Endpoint for retrieving a page of address objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link AddressModel}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping
    public ResponseEntity<PagedModel<AddressModel>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(AddressModel.class)
        Pageable pageable) {

        log.debug("Getting member page for pageable:  {}", pageable );

        var page = addressService.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single address from the database.
     * @param id The id of the address.
     * @return @return A {@link ResponseEntity} containing a {@link AddressModel}
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}")
    public ResponseEntity<AddressModel> get(@PathVariable int id) {
        log.debug("Getting address with id: {}", id);

        var ret = addressService.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of addresses in the database.
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(addressService.getAddressCount()));
    }

    /**
     * Endpoint for creating an address.
     * @param address The {@link AddressModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link AddressModel} representing the newly created object.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressModel> create(
        @NotNull @Valid @RequestBody AddressModel address) {
        log.debug("Creating address, data: {}", address);

        var created = addressService.createAddress(address);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * Endpoint for updating an address.
     * @param id The id of the address to update in the database.
     * @param address The {@link AddressModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link AddressModel} that represents the updated address.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressModel> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody AddressModel address) {
        log.debug("Updating address, id: {}, data: {}", id, address);

        var ret = addressService.updateAddress(id, address);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting an address from the database.
     * @param id The id of the address to delete
     * @return an empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting address, id: {}", id);
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
