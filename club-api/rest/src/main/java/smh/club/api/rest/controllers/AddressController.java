package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.AddressService;
import com.smh.club.api.rest.dto.address.AddressCreateDto;
import com.smh.club.api.rest.dto.address.AddressDto;
import com.smh.club.api.rest.dto.address.AddressFullNameDto;
import com.smh.club.api.rest.dto.address.AddressUpdateDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PagedDto;
import com.smh.club.api.rest.validation.constraints.SortConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Defines REST endpoints that targets address objects in the database.
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressController {

    private final String DEFAULT_SORT = "id";

    private final AddressService addressSvc;

    /**
     * Endpoint for retrieving a page of address objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a page of {@link AddressFullNameDto}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping
    public ResponseEntity<PagedDto<AddressFullNameDto>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(AddressFullNameDto.class)
        Pageable pageable) {

        var page = addressSvc.getPage(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single address from the database.
     *
     * @param id The id of the address.
     * @return @return A {@link ResponseEntity} containing a {@link AddressDto}
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}")
    public ResponseEntity<AddressDto> get(@PathVariable int id) {
        var ret = addressSvc.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of addresses in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {

        return ResponseEntity.ok(CountResponse.of(addressSvc.getAddressCount()));
    }

    /**
     * Endpoint for creating an address.
     *
     * @param address The {@link AddressDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing an {@link AddressDto} representing the newly created object.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PostMapping
    public ResponseEntity<AddressDto> create(
        @NotNull @Valid @RequestBody AddressCreateDto address) {

        return ResponseEntity.status(HttpStatus.CREATED).body(addressSvc.createAddress(address));
    }

    /**
     * Endpoint for updating an address.
     *
     * @param id The id of the address to update in the database.
     * @param address The {@link AddressDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing an {@link AddressDto} that represents the updated address.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @PutMapping("{id}")
    public ResponseEntity<AddressDto> update(
        @PathVariable int id,
        @NotNull @Valid @RequestBody AddressUpdateDto address) {

        var ret = addressSvc.updateAddress(id, address);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting an address from the database.
     *
     * @param id The id of the address to delete
     * @return an empty {@link ResponseEntity}.
     */
    @PreAuthorize("hasAuthority('permission:write')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        addressSvc.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
