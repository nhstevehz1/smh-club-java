package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.contracts.services.PhoneService;
import com.smh.club.api.hateoas.models.PhoneModel;
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
 * Defines REST endpoints that targets phone objects in the database.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v2/phones", produces = MediaTypes.HAL_JSON_VALUE)
public class PhoneController {

    private final String DEFAULT_SORT = "id";

    private final PhoneService phoneService;

    /**
     * Endpoint for retrieving a page of phone objects from the database.
     * if no sort is specified then the DEFAULT_SORT is used.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link ResponseEntity} containing a {@link PagedModel} of type {@link PhoneModel}.
     */
    @GetMapping
    public ResponseEntity<PagedModel<PhoneModel>> page(
        @PageableDefault(sort = {DEFAULT_SORT})
        @SortConstraint(dtoClass = PhoneModel.class)
        Pageable pageable) {

        log.debug("Getting member page for pageable:  {}", pageable );

        var page = phoneService.getPage(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint for retrieving a single phone from the database.
     *
     * @param id The id of the phone.
     * @return @return A {@link ResponseEntity} containing a {@link PhoneModel}
     */
    @GetMapping("{id}")
    public ResponseEntity<PhoneModel> get(@PathVariable int id) {
        log.debug("Getting phone with id: {}", id);

        var ret = phoneService.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of phonees in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(phoneService.getPhoneCount()));
    }

    /**
     * Endpoint for creating a phone.
     *
     * @param phone The {@link PhoneModel} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link PhoneModel} representing the newly created object.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneModel> create(@RequestBody PhoneModel phone) {
        log.debug("Creating phone, data: {}", phone);

        var created = phoneService.createPhone(phone);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    /**
     * Endpoint for updating a phone.
     *
     * @param id The id of the phone to update in the database.
     * @param phone The {@link PhoneModel} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link PhoneModel} that represents the updated phone.
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneModel> update(@PathVariable int id, @RequestBody PhoneModel phone) {
        log.debug("Updating phone, id: {}, data: {}", id, phone);

        phone.setId(0);
        var ret = phoneService.updatePhone(id, phone);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a phone from the database.
     *
     * @param id The id of the phone to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        log.debug("Deleting phone, id: {}", id);
        phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
